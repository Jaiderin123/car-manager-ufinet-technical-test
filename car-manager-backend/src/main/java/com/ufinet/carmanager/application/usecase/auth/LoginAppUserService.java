package com.ufinet.carmanager.application.usecase.auth;

import com.ufinet.carmanager.domain.auth.ports.in.LoginAppUserUseCase;
import com.ufinet.carmanager.domain.auth.ports.out.JwtProvider;
import com.ufinet.carmanager.domain.shared.exceptions.AuthException;
import com.ufinet.carmanager.domain.user.ports.out.AppUserRepository;
import com.ufinet.carmanager.domain.auth.ports.out.IPasswordEncoderPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class LoginAppUserService implements LoginAppUserUseCase {
    private final AppUserRepository appUserRepository;
    private final IPasswordEncoderPort passwordEncoderPort;
    private final JwtProvider jwtProvider;

    @Override
    public Mono<String> loginAppUser(String email, String password) {
        return appUserRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new AuthException()))
                .flatMap(userApp ->{
                    boolean passwordMatches = passwordEncoderPort.matches(password, userApp.password());
                    if (!passwordMatches)
                        return Mono.error(new AuthException());

                    return Mono.just(jwtProvider.generateAuthToken(userApp.id()));
                });
    }
}
