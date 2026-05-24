package com.ufinet.carmanager.infrastructure.adapters.persistence.car.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("cars")
public class CarEntity {
    @Id
    @Column("id")
    private Long carId;

    @Column("user_id")
    private Long userId;

    private String brand;

    private String model;

    private int year;

    private String plate;

    private String color;

    @Column("photo_url")
    private String photoUrl;

    @Column("is_active")
    private boolean isActive;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;
    
}