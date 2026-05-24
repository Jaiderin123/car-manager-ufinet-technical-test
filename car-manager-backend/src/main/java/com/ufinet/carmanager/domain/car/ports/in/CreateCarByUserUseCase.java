package com.ufinet.carmanager.domain.car.ports.in;

import com.ufinet.carmanager.domain.car.model.Car;
import reactor.core.publisher.Mono;

public interface CreateCarByUserUseCase {

    Mono<Car> createCarByUserId(Car requestCar);
}
