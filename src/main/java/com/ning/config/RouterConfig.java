package com.ning.config;

import com.ning.handler.ClusterHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routerCluster(ClusterHandler handler) {
        return route(RequestPredicates.GET("/sloth/im/access")
                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), handler::access);
    }

}
