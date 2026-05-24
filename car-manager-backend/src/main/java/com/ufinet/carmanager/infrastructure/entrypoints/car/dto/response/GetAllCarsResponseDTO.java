package com.ufinet.carmanager.infrastructure.entrypoints.car.dto.response;

import java.time.LocalDateTime;

public record GetAllCarsResponseDTO(
        Long carId,
        String brand,
        String model,
        int year,
        String plate,
        String color,
        String photoUrl,
        Boolean isActive,
        LocalDateTime createdAt
) {}