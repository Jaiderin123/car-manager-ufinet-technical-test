package com.ufinet.carmanager.application.usecase.cars;

import com.ufinet.carmanager.domain.car.model.Car;
import com.ufinet.carmanager.domain.car.ports.in.GetAllCarsByUserUseCase;
import com.ufinet.carmanager.domain.car.ports.out.CarRepository;
import com.ufinet.carmanager.domain.shared.exceptions.BusinessException;
import com.ufinet.carmanager.domain.user.ports.out.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class GetAllCarsByUserService implements GetAllCarsByUserUseCase {
    private final AppUserRepository appUserRepository;
    private final CarRepository carRepository;

    @Override
    public Flux<Car> getAllCarsByUserId(Long userId) {
        return appUserRepository.existsById(userId)
                .flatMapMany(exists -> {
                    if (!exists)
                        return Mono.error(new BusinessException("User not found"));

                    return carRepository.findAllByUserId(userId);
                });
    }
}
