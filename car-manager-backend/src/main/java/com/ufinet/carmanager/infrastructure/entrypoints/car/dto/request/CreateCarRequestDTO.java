package com.ufinet.carmanager.infrastructure.entrypoints.car.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateCarRequestDTO(

        @NotBlank(message = "Brand is required")
        @Size(max = 50, message = "Brand must not exceed 50 characters")
        String brand,

        @NotBlank(message = "Model is required")
        @Size(max = 50, message = "Model must not exceed 50 characters")
        String model,

        @Min(value = 1900, message = "Year must be at least 1900")
        //@Max(value = 2027, message = "Year cannot be in the future")  // current year + 1
        int year,

        @NotBlank(message = "Plate is required")
        @Size(max = 10, message = "Plate must not exceed 10 characters")
        @Pattern(
                regexp = "^[A-Z]{3}-[0-9]{3}$",
                message = "Plate must follow Colombian format: ABC-123"
        )
        String plate,

        @NotBlank(message = "Color is required")
        @Size(max = 30, message = "Color must not exceed 30 characters")
        String color,

        @Size(max = 500, message = "Photo URL must not exceed 500 characters")
        String photoUrl  // nullable, no @NotBlank

) { }