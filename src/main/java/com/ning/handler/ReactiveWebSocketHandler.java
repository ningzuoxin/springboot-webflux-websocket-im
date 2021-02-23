package com.ning.handler;

import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * WebSocketHandler是用来处理WebSocket Session的接口，在WebFlux中我们需要创建自己的Handler实现这个接口。
 * 实现WebSocketHandler主要是要重写Mono<Void> handle(WebSocketSession session)方法。
 * 这个方法会在创建WebSocket连接后被调用，入参session就是对应的WebSocketSession，一般就是要处理这个session来实现特定的功能。
 */
@Slf4j
@Component("ReactiveWebSocketHandler")
public class ReactiveWebSocketHandler implements WebSocketHandler {

    private final ReactiveSessions sessions;

    @Autowired
    public ReactiveWebSocketHandler(ReactiveSessions sessions) {
        this.sessions = sessions;
    }

    @Override
    public @NotNull
    Mono<Void> handle(final @NotNull WebSocketSession webSocketSession) {
        log.info("ReactiveWebSocketHandler # handle webSocketSession={}", webSocketSession);
        return sessions.handle(webSocketSession);
    }

}
