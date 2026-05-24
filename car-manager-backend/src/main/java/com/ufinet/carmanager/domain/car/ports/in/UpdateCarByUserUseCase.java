package com.ufinet.carmanager.domain.car.ports.in;

import com.ufinet.carmanager.domain.car.model.Car;
import reactor.core.publisher.Mono;

public interface UpdateCarByUserUseCase {
    Mono<Car> updateCarByUserId(Car updatedCar);
}
