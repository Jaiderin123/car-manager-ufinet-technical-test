package com.ufinet.carmanager.infrastructure.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarecolombia.projectmanager.infrastructure.entrypoints.config.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class AccessDeniedHandler implements ServerAccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException ade) {
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return Mono.fromCallable(() ->
                        objectMapper.writeValueAsBytes(
                                new ApiResponse<>(HttpStatus.FORBIDDEN.value(), ade.getMessage())
                        ))
                .flatMap(bytes -> {
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                    return exchange.getResponse().writeWith(Mono.just(buffer));
                });
    }
}