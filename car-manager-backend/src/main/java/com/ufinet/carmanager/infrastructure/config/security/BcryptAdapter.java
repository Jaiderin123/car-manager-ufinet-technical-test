package com.ufinet.carmanager.infrastructure.config.security;

import com.softwarecolombia.projectmanager.domain.shared.exceptions.ports.IPasswordEncoderPort;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@AllArgsConstructor
public class BcryptAdapter implements IPasswordEncoderPort {
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<String> encode(String rawPassword) {
        return Mono.just(passwordEncoder.encode(rawPassword));
    }

    @Override
    public Mono<Boolean> matches(String rawPassword, String encodedPassword) {
        return Mono.fromCallable(() -> passwordEncoder.matches(rawPassword, encodedPassword))
                .subscribeOn(Schedulers.boundedElastic());
    }
}