package com.ufinet.carmanager.domain.car.ports.in;

import com.ufinet.carmanager.domain.car.model.Car;
import reactor.core.publisher.Flux;

public interface GetAllCarsByUserUseCase {
    Flux<Car> getAllCarsByUserId(Long userId);
}
