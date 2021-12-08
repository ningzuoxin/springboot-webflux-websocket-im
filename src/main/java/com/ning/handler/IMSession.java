package com.ning.handler;

import com.ning.message.ClientMessage;
import com.ning.repo.OnlineRepo;
import com.ning.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class IMSession {

    public String id;

    AtomicBoolean isActive;
    AtomicBoolean isConnected;

    private final OnlineRepo onlineRepo;
    private final WebSocketSession session;
    private final int bufferSize = 100;
    private final EmitterProcessor<WebSocketMessage> emitterProcessor = EmitterProcessor.create(bufferSize);

    private final String env;

    public IMSession(WebSocketSession session, OnlineRepo onlineRepo, String env) {
        this.onlineRepo = onlineRepo;
        this.session = session;
        this.env = env;
    }

    public boolean auth() {
        String query = session.getHandshakeInfo().getUri().getQuery();

        Map<String, String> params = Utils.getParamsFromQueryUrl(query);
        String id = params.get("id");
        String token = params.get("token");

        // js没有提供添加header参数的api
        // String id = session.getHandshakeInfo().getHeaders().getFirst("id");
        // String token = session.getHandshakeInfo().getHeaders().getFirst("token");

        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(token)) {
            return false;
        }

        this.id = Utils.URLDecoderParam(id);
        token = Utils.URLDecoderParam(token);

        this.isActive = new AtomicBoolean(false);
        this.isConnected = new AtomicBoolean(false);
        if (onlineRepo.auth(this.id, token, this)) {
            start();
            return true;
        }
        return false;
    }

    public Mono<Void> create() {
        isConnected.set(true);
        Flux<WebSocketMessage> outSource = emitterProcessor.map(data -> data);
        Mono<Void> input = session.receive()
                .doOnSubscribe(s -> {
                })
                .doOnRequest(r -> {
                })
                .doOnNext(message -> {
                    if (message.getType() == WebSocketMessage.Type.TEXT) {
                        sendTestMessageToAllOnlineClient(message.getPayloadAsText());
                    }
                })
                .doOnComplete(() -> {
                })
                .doOnCancel(() -> {
                })
                .doOnTerminate(() -> this.removeClient())
                .doOnError(err -> log.error("IMSession # doOnError error={}", err.getMessage())).then();
        Mono<Void> output = session.send(outSource);
        return Mono.zip(input, output).then();
    }

    private void start() {
        isActive.set(true);
    }

    private void removeClient() {
        isActive.set(false);
        isConnected.set(false);
        if (onlineRepo.offline(id)) {
            log.info("IMSession # removeClient id={} removed", this.id);
        }
    }

    private void destroy(CloseStatus closeStatus) {
        session.close(closeStatus).subscribe();
    }

    private void closeMe(CloseStatus closeStatus) {
        if (isConnected.get()) {
            removeClient();
            destroy(closeStatus);
        }
    }

    public void sendMessageToClient(String payload, int business, ClientMessage.Type type) {
        String p = "";
        if (payload != null) {
            p = payload;
        }
        ClientMessage.Message msg = ClientMessage.Message.newBuilder()
                .setTimestamp(Utils.nowSecond())
                .setType(type)
                .setBusiness(business)
                .setPayload(p)
                .build();
        emitterProcessor.onNext(session.binaryMessage(factory ->
                factory.wrap(msg.toByteArray()))
        );
    }

    public void sendTestMessageToClient(String message) {
        emitterProcessor.onNext(session.textMessage(message));
    }

    public void sendPing() {
        emitterProcessor.onNext(session.pingMessage(factory ->
                factory.wrap(new byte[0])));
    }

    /**
     * 发送文本消息至所有在线的客户端
     *
     * @param message
     */
    public void sendTestMessageToAllOnlineClient(String message) {
        onlineRepo.findTerminalClients("0").forEach(client -> {
            String sessionId = client.session.id;
            if (!id.equals(sessionId)) {
                client.session.sendTestMessageToClient(message);
            }
        });
    }

}
