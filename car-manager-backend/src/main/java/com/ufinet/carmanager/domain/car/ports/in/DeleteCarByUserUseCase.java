package com.ufinet.carmanager.domain.car.ports.in;

import reactor.core.publisher.Mono;

public interface DeleteCarByUserUseCase {
    Mono<Void> deleteCar(Long userId, Long carId);
}
