package com.ufinet.carmanager.application.usecase.cars;

import com.ufinet.carmanager.domain.car.model.Car;
import com.ufinet.carmanager.domain.car.ports.in.CreateCarByUserUseCase;
import com.ufinet.carmanager.domain.car.ports.out.CarRepository;
import com.ufinet.carmanager.domain.shared.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.ufinet.carmanager.domain.shared.utils.DateUtils.isFutureYear;

@Service
@AllArgsConstructor
public class CreateCarByUserService implements CreateCarByUserUseCase {
    private final CarRepository carRepository;

    @Override
    public Mono<Car> createCarByUserId(Car requestCar) {
        if (isFutureYear(requestCar.year()))
            return Mono.error(new BusinessException("Invalid car year, can't be greater than the actual year + 1"));

        return carRepository.create(requestCar);
    }
}
