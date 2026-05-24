package com.ufinet.carmanager.infrastructure.adapters.persistence.car.r2dbc;

import com.ufinet.carmanager.infrastructure.adapters.persistence.car.entity.CarEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface CarR2dbcRepository extends R2dbcRepository<CarEntity, Long> {

    Flux<CarEntity> findByUserId(Long userId);

    @Modifying
    @Query("""
        UPDATE cars
        SET brand = :brand,
            model = :model,
            year = :year,
            plate = :plate,
            color = :color,
            photo_url = :photoUrl,
            updated_at = :updateAt
        WHERE id = :id
        AND user_id = :userId
    """)
    Mono<Integer> updateCarByIdAndUserId(
            @Param("id") Long id,
            @Param("userId") Long userId,
            @Param("brand") String brand,
            @Param("model") String model,
            @Param("year") int year,
            @Param("plate") String plate,
            @Param("color") String color,
            @Param("photoUrl") String photoUrl,
            @Param("updateAt") LocalDateTime updateAt
    );

    Mono<CarEntity> findByCarIdAndUserId(Long carId, Long userId);

    @Modifying
    @Query("""
        UPDATE cars
        SET is_active = :newStatus,
            updated_at = :updateAt
        WHERE id = :carId
        AND user_id = :userId
    """)
    Mono<Integer> updateCarStatus(
            @Param("carId") Long carId,
            @Param("userId") Long userId,
            @Param("newStatus") Boolean newStatus,
            @Param("updateAt") LocalDateTime updateAt
    );

    @Modifying
    @Query("DELETE FROM cars WHERE id = :carId AND user_id = :userId")
    Mono<Integer> deleteByCarIdAndUserId(@Param("carId") Long carId, @Param("userId") Long userId);

}
