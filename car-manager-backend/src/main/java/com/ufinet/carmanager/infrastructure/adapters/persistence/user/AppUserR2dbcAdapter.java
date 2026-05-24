package com.ufinet.carmanager.infrastructure.adapters.persistence.user;

import com.ufinet.carmanager.domain.user.ports.out.AppUserRepository;
import com.ufinet.carmanager.domain.user.model.AppUser;
import com.ufinet.carmanager.infrastructure.adapters.persistence.user.mapper.UserMapper;
import com.ufinet.carmanager.infrastructure.adapters.persistence.user.r2dbc.AppUserR2dbcRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class AppUserR2dbcAdapter implements AppUserRepository {
    private final AppUserR2dbcRepository appUserR2DbcRepository;
    private final UserMapper userMapper;

    @Override
    public Mono<AppUser> findByEmail(String email) {
        return appUserR2DbcRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(Long userId) {
        return appUserR2DbcRepository.existsById(userId);
    }

    @Override
    public Mono<AppUser> findById(Long userId) {
        return appUserR2DbcRepository.findById(userId)
                .map(userMapper::toDomain);
    }
}
