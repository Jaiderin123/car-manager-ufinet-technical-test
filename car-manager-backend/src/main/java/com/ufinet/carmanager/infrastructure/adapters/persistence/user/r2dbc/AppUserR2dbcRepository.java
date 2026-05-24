package com.ufinet.carmanager.infrastructure.adapters.persistence.user.r2dbc;

import com.ufinet.carmanager.infrastructure.adapters.persistence.user.entity.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface AppUserR2dbcRepository extends R2dbcRepository<UserEntity, Long> {
    Mono<UserEntity> findByEmail(String email);
}
