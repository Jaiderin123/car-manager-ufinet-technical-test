package com.ufinet.carmanager.infrastructure.entrypoints.car.mapper;

import com.ufinet.carmanager.domain.car.model.Car;
import com.ufinet.carmanager.infrastructure.entrypoints.car.dto.request.CreateCarRequestDTO;
import com.ufinet.carmanager.infrastructure.entrypoints.car.dto.request.UpdateCarRequestDTO;
import com.ufinet.carmanager.infrastructure.entrypoints.car.dto.response.CreateCarResponseDTO;
import com.ufinet.carmanager.infrastructure.entrypoints.car.dto.response.GetAllCarsResponseDTO;
import com.ufinet.carmanager.infrastructure.entrypoints.car.dto.response.UpdateCarResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CarDTOMapper {

    @Mapping(target = "carId", ignore = true)
    @Mapping(target = "isActive", expression = "java(Boolean.TRUE)")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "brand", source = "dto.brand")
    @Mapping(target = "model", source = "dto.model")
    @Mapping(target = "year", source = "dto.year")
    @Mapping(target = "plate", source = "dto.plate")
    @Mapping(target = "color", source = "dto.color")
    @Mapping(target = "photoUrl", source = "dto.photoUrl")
    Car toDomain(Long userId, CreateCarRequestDTO dto);

    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "carId", source = "carId")
    @Mapping(target = "brand", source = "dto.brand")
    @Mapping(target = "model", source = "dto.model")
    @Mapping(target = "year", source = "dto.year")
    @Mapping(target = "plate", source = "dto.plate")
    @Mapping(target = "color", source = "dto.color")
    @Mapping(target = "photoUrl", source = "dto.photoUrl")
    Car toDomain(Long userId, Long carId, UpdateCarRequestDTO dto);

    GetAllCarsResponseDTO toGetAllCarsResponseDTO(Car car);

    CreateCarResponseDTO toCreateResponseDTO(Car car);

    UpdateCarResponseDTO toUpdateResponseDTO(Car car);


}