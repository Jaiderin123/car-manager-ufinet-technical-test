package com.ufinet.carmanager.domain.car.model;

import java.time.LocalDateTime;

public record Car(
        Long userId,
        Long carId,
        String brand,
        String model,
        int year,
        String plate,
        String color,
        String photoUrl,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
