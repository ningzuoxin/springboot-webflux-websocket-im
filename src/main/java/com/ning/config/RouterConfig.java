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

    @Bean
    public RouterFunction<ServerResponse> routerSubscribe(ClusterHandler handler) {
        return route(RequestPredicates.POST("/sloth/im/temporary/subscribe")
                .and(RequestPredicates.accept(MediaType.APPLICATION_FORM_URLENCODED)), handler::subscribe);
    }

    @Bean
    public RouterFunction<ServerResponse> routerUnsubscribe(ClusterHandler handler) {
        return route(RequestPredicates.POST("/sloth/im/temporary/unsubscribe")
                .and(RequestPredicates.accept(MediaType.APPLICATION_FORM_URLENCODED)), handler::unSubscribe);
    }

    @Bean
    public RouterFunction<ServerResponse> routerCreateSubscribe(ClusterHandler handler) {
        return route(RequestPredicates.POST("/sloth/im/temporary/create")
                .and(RequestPredicates.accept(MediaType.APPLICATION_FORM_URLENCODED)), handler::createSubscribe);
    }

    @Bean
    public RouterFunction<ServerResponse> routerDestroyUnsubscribe(ClusterHandler handler) {
        return route(RequestPredicates.POST("/sloth/im/temporary/destroy")
                .and(RequestPredicates.accept(MediaType.APPLICATION_FORM_URLENCODED)), handler::destroySubscribe);
    }

}
