package com.ufinet.carmanager.infrastructure.config.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;


@Component
public class SecurityUtils {
    public Mono<Long> extractUserId(ServerRequest request) {
        return request.principal()
                .cast(UsernamePasswordAuthenticationToken.class)
                .map(UsernamePasswordAuthenticationToken::getPrincipal)
                .cast(Long.class);
    }
}
