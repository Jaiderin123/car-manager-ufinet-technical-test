package com.ufinet.carmanager.application.usecase.cars;

import com.ufinet.carmanager.domain.car.model.Car;
import com.ufinet.carmanager.domain.car.ports.in.DeleteCarByUserUseCase;
import com.ufinet.carmanager.domain.car.ports.out.CarRepository;
import com.ufinet.carmanager.domain.shared.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class DeleteCarByUserService implements DeleteCarByUserUseCase {
    private final CarRepository carRepository;
    @Override
    public Mono<Void> deleteCar(Long userId, Long carId) {
        return carRepository.delete(userId, carId)
                .flatMap(rowsAffected -> {
                    if (rowsAffected == 0)
                        return Mono.error(new BusinessException("Car not found"));

                    return Mono.empty();
                })
                .then();
    }
}
