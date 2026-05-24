package com.ufinet.carmanager.infrastructure.config.security;

import com.ufinet.carmanager.domain.auth.ports.out.IPasswordEncoderPort;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BcryptAdapter implements IPasswordEncoderPort {
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}