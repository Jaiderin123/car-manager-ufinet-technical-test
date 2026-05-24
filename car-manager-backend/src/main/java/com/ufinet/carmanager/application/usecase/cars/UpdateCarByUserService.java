package com.ufinet.carmanager.application.usecase.cars;

import com.ufinet.carmanager.domain.car.model.Car;
import com.ufinet.carmanager.domain.car.ports.in.UpdateCarByUserUseCase;
import com.ufinet.carmanager.domain.car.ports.out.CarRepository;
import com.ufinet.carmanager.domain.shared.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static com.ufinet.carmanager.domain.shared.utils.DateUtils.isFutureYear;

@Service
@AllArgsConstructor
public class UpdateCarByUserService implements UpdateCarByUserUseCase {
    private final CarRepository carRepository;

    @Override
    public Mono<Car> updateCarByUserId(Car updatedCar) {
        if (isFutureYear(updatedCar.year()))
            return Mono.error(new BusinessException(
                    "Invalid car year, can't be greater than the actual year + 1"));

        return carRepository.updateCar(updatedCar)
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated == 0) {
                        return Mono.error(new BusinessException(
                                "Car not found or does not belong to the user"));
                    }
                    return carRepository.findByCarId(updatedCar.carId());
                });
    }
}