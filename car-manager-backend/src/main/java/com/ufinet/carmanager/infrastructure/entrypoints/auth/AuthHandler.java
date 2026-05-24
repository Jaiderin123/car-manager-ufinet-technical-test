package com.ufinet.carmanager.infrastructure.entrypoints.auth;

import com.ufinet.carmanager.domain.auth.ports.in.LoginAppUserUseCase;
import com.ufinet.carmanager.infrastructure.entrypoints.auth.dto.request.LoginRequestDTO;
import com.ufinet.carmanager.infrastructure.entrypoints.config.ApiResponse;
import com.ufinet.carmanager.infrastructure.entrypoints.config.validators.ServerRequestValidator;
import com.ufinet.carmanager.infrastructure.entrypoints.exceptions.ServerRequestValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@AllArgsConstructor
public class AuthHandler {
    private final ServerRequestValidator serverRequestValidator;
    private final LoginAppUserUseCase loginUserUseCase;

    public Mono<ServerResponse> listenPOSTLogin(ServerRequest serverRequest){
        return serverRequest.bodyToMono(LoginRequestDTO.class)
                .switchIfEmpty(Mono.error(new ServerRequestValidationException("User can't be empty")))
                .flatMap(serverRequestValidator::validate)
                .flatMap(loginRequestDTO -> loginUserUseCase.loginAppUser(loginRequestDTO.email(), loginRequestDTO.password()))
                .flatMap(responseData ->
                        ServerResponse.ok()
                                .contentType(APPLICATION_JSON)
                                .bodyValue(new ApiResponse<>(responseData,200,"User logged successfully"))
                );
    }

}
