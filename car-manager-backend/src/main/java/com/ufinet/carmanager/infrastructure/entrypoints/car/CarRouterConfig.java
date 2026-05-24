package com.ufinet.carmanager.infrastructure.entrypoints.car;

import com.ufinet.carmanager.infrastructure.config.AppUrlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
public class CarRouterConfig {

    private static final String CAR_BASE_PATH = "/car";
    private static final String CAR_ID = "/{carId}";

    @Bean
    public RouterFunction<ServerResponse> carRoutes(CarHandler carHandler, AppUrlConfig appUrlConfig) {
        String basePath = appUrlConfig.getBaseUrl() + CAR_BASE_PATH;
        return nest(
                path(basePath),
                route(
                        GET("/"),               carHandler::listenGETCars)
                        .andRoute(POST("/"),    carHandler::listenPOSTCreateCar)
                        .andRoute(PUT(CAR_ID),         carHandler::listenPUTUpdateCar)
                        .andRoute(PATCH(CAR_ID),       carHandler::listenPATCHUpdateCar)
                        .andRoute(DELETE(CAR_ID),      carHandler::listenDELETECar)
        );
    }
}