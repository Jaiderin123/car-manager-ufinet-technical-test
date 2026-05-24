package com.ufinet.carmanager.application.usecase.cars;

import com.ufinet.carmanager.domain.car.ports.in.UpdateCarStatusByUserUseCase;
import com.ufinet.carmanager.domain.car.ports.out.CarRepository;
import com.ufinet.carmanager.domain.shared.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UpdateCarStatusByUserService implements UpdateCarStatusByUserUseCase {
    private final CarRepository carRepository;

    @Override
    public Mono<Void> updateCarStatus(Long userId, Long carId) {
        return carRepository.getActualCarStatus(userId, carId)
                .switchIfEmpty(Mono.error(new BusinessException("Car not found or does not belong to the user")))
                .flatMap(carActualStatus -> carRepository.updateCarStatus(userId, carId, !carActualStatus));
    }
}
