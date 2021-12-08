package com.ning.handler;

import com.ning.repo.OnlineRepo;
import com.ning.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.CloseStatus;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class ReactiveSessions {

    private final OnlineRepo onlineRepo;
    private final String env;

    @Autowired
    public ReactiveSessions(OnlineRepo onlineRepo) {
        this.onlineRepo = onlineRepo;
        this.env = PropertiesUtil.getProperties("application.properties").getProperty("environment");
    }

    public Mono<Void> handle(WebSocketSession session) {
        IMSession s = new IMSession(session, onlineRepo, env);
        if (s.auth()) {
            return s.create();
        }
        return session.close(CloseStatus.NOT_ACCEPTABLE.withReason("auth error"));
    }

}
