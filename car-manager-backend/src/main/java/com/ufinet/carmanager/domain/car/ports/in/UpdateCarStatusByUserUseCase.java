package com.ufinet.carmanager.domain.car.ports.in;

import reactor.core.publisher.Mono;

public interface UpdateCarStatusByUserUseCase {
    Mono<Void> updateCarStatus(Long userId, Long carId);
}
