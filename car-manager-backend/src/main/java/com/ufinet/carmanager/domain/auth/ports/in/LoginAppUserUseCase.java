package com.ufinet.carmanager.domain.auth.ports.in;

import reactor.core.publisher.Mono;

public interface LoginAppUserUseCase {
    Mono<String> loginAppUser(String email, String password);
}
