package com.ufinet.carmanager.infrastructure.adapters.persistence.car.mapper;

import com.ufinet.carmanager.domain.car.model.Car;
import com.ufinet.carmanager.infrastructure.adapters.persistence.car.entity.CarEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarMapper {

    @Mapping(target = "isActive", expression = "java(carEntity.isActive())")
    Car toDomain(CarEntity carEntity);

    @Mapping(target = "isActive", expression = "java(car.isActive())")
    CarEntity toEntity(Car car);
}