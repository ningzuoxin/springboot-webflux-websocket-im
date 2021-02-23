package com.ning.handler;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.InvalidProtocolBufferException;
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

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class IMSession {

    public String id;

    AtomicBoolean isActive;
    AtomicBoolean isConnected;

    private final OnlineRepo onlineRepo;
    private final WebSocketSession session;
    private final int bufferSize = 10;
    private final EmitterProcessor<WebSocketMessage> emitterProcessor = EmitterProcessor.create(bufferSize);

    private final String env;

    public IMSession(WebSocketSession session, OnlineRepo onlineRepo, String env) {
        this.onlineRepo = onlineRepo;
        this.session = session;
        this.env = env;
    }

    public boolean auth() {
        String query = session.getHandshakeInfo().getUri().getQuery();
        log.info("IMSession # auth query={}", query);

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
        log.info("IMSession # auth id={},token={}", this.id, token);

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
                .doOnNext(message -> {
                    if (message.getType() == WebSocketMessage.Type.TEXT) {
                        if (env.equals("dev") || env.equals("test")) {
                            // Echo Test
                            sendTestMessageToAllOnlineClient(message.getPayloadAsText());
                            // sendTestMessageToClient("服务端回复：" + message.getPayloadAsText());
                            // sendMessageToClient(message.getPayloadAsText(), -1);
                        } else {
                            closeMe(CloseStatus.BAD_DATA.withReason("not accept text message"));
                        }
                    } else if (message.getType() == WebSocketMessage.Type.BINARY) {
                        ByteBuffer buffer = message.getPayload().asByteBuffer();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes, 0, bytes.length);
                        ClientMessage.Message msg = null;
                        try {
                            msg = ClientMessage.Message.parseFrom(bytes);
                        } catch (InvalidProtocolBufferException e) {
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                        }
                        if (msg != null) {
                            switch (msg.getType()) {
                                case BIND_PHONE_NO: // 手机号登录绑定
                                    if (onlineRepo.bindPhoneNo(id, msg.getPayload())) {
                                        sendMessageToClient(ClientMessage.Type.BIND_PHONE_NO.toString(), 1, ClientMessage.Type.COMMAND);
                                    } else {
                                        sendMessageToClient(ClientMessage.Type.BIND_PHONE_NO.toString(), 0, ClientMessage.Type.COMMAND);
                                    }
                                    break;
                                case BIND_CUSTOM_NO: // 客户号登录绑定
                                    if (onlineRepo.bindCustomNo(id, msg.getPayload())) {
                                        sendMessageToClient(ClientMessage.Type.BIND_CUSTOM_NO.toString(), 1, ClientMessage.Type.COMMAND);
                                    } else {
                                        sendMessageToClient(ClientMessage.Type.BIND_CUSTOM_NO.toString(), 0, ClientMessage.Type.COMMAND);
                                    }
                                    break;
                                case COMMAND:
                                    break;
                                default:
                                    closeMe(CloseStatus.NOT_ACCEPTABLE.withReason("closed by service because type not accept"));
                            }
                        }
                    }
                })
                .doOnComplete(this::removeClient)
                .doOnCancel(this::removeClient)
                .doOnTerminate(this::removeClient)
                .doOnError(err -> log.info("IMSession # doOnError error={}", err.getMessage())).then();
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

    public void sendMessageToClient(String payload, int business) {
        sendMessageToClient(payload, business, ClientMessage.Type.BUSINESS);
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

    public void sendCloseMessage() {
        sendMessageToClient("", 0, ClientMessage.Type.CLOSE);
    }

    public void forceCloseSession() {
        closeMe(CloseStatus.GOING_AWAY.withReason("don't reconnect me"));
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
            log.info("IMSession # sendTestMessageToAllOnlineClient client={}", JSON.toJSONString(client));
            String sessionId = client.session.id;
            if (!id.equals(sessionId)) {
                client.session.sendTestMessageToClient(message);
            }
        });
    }

}
