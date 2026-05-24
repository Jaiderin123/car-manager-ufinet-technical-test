package com.ufinet.carmanager.infrastructure.adapters.persistence.car;

import com.ufinet.carmanager.domain.car.model.Car;
import com.ufinet.carmanager.domain.car.ports.out.CarRepository;
import com.ufinet.carmanager.infrastructure.adapters.persistence.car.entity.CarEntity;
import com.ufinet.carmanager.infrastructure.adapters.persistence.car.mapper.CarMapper;
import com.ufinet.carmanager.infrastructure.adapters.persistence.car.r2dbc.CarR2dbcRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.ufinet.carmanager.domain.shared.utils.DateUtils.nowColombia;

@Repository
@AllArgsConstructor
public class CarR2dbcAdapter implements CarRepository {
    private final CarR2dbcRepository carR2dbcRepository;
    private final CarMapper carMapper;

    @Override
    public Flux<Car> findAllByUserId(Long userId) {
        return carR2dbcRepository.findByUserId(userId)
                .map(carMapper::toDomain);
    }

    @Override
    public Mono<Car> create(Car car) {
        return carR2dbcRepository.save(carMapper.toEntity(car))
                .map(carMapper::toDomain);
    }

    @Override
    public Mono<Integer> updateCar(Car updateCar) {
        return carR2dbcRepository.updateCarByIdAndUserId(
                updateCar.carId(), updateCar.userId(), updateCar.brand(),
                updateCar.model(), updateCar.year(), updateCar.plate(), updateCar.color(),
                updateCar.photoUrl(), nowColombia()
                );
    }

    @Override
    public Mono<Car> findByCarId(Long carId) {
        return carR2dbcRepository.findById(carId)
                .map(carMapper::toDomain);
    }

    @Override
    public Mono<Boolean> getActualCarStatus(Long userId, Long carId) {
        return carR2dbcRepository.findByCarIdAndUserId(carId, userId)
                .map(CarEntity::isActive);
    }

    @Override
    public Mono<Void> updateCarStatus(Long userId, Long carId, boolean newStatus) {
        return carR2dbcRepository.updateCarStatus(carId, userId, newStatus, nowColombia())
                .then();
    }

    @Override
    public Mono<Integer> delete(Long userId, Long carId) {
        return carR2dbcRepository.deleteByCarIdAndUserId(carId, userId);
    }
}
