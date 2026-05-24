package com.ufinet.carmanager.infrastructure.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufinet.carmanager.domain.shared.utils.ValidateNullParam;
import com.ufinet.carmanager.infrastructure.entrypoints.config.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
    private final SecurityProperties securityProperties;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestPath = exchange.getRequest().getURI().getPath();

        // Validate public routes
        if (securityProperties.getPublicPaths().stream().anyMatch(requestPath::startsWith))
            return chain.filter(exchange);

        // Validate JWT
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer "))
            return unauthorized(exchange, "Access Denied, token is empty or invalid");

        String token = header.substring(7);
        JwtUtil.TokenValidationResult tokenValidationResult = jwtUtil.validate(token);
        if (tokenValidationResult != JwtUtil.TokenValidationResult.VALID)
            return unauthorized(exchange, tokenValidationResult.toMessage());


        Long userId = ValidateNullParam.orDefault(jwtUtil.extractUserId(token), 0L);

        //AuthPrincipal principal = new AuthPrincipal(userId, workspaceId, role);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());

        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return Mono.fromCallable(() ->
                        objectMapper.writeValueAsBytes(
                                new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), message)
                        ))
                .flatMap(bytes -> {
                    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                    return exchange.getResponse().writeWith(Mono.just(buffer));
                });
    }
}
