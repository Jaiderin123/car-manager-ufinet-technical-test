package com.ufinet.carmanager.infrastructure.entrypoints.auth;

import com.ufinet.carmanager.infrastructure.config.AppUrlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
public class AuthRouterConfig {

    @Bean
    public RouterFunction<ServerResponse> authRoutes(AuthHandler authHandler, AppUrlConfig appUrlConfig) {
        //log.info("Franchise base Path APP -> {}", basePath);
        return nest(
                path(appUrlConfig.getBaseUrl() + "/auth"),
                route(
                        POST("/login"),
                        authHandler::listenPOSTLogin
                )
        );
    }
}
