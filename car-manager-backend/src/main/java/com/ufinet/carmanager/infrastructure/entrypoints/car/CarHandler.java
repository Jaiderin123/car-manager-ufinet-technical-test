package com.ufinet.carmanager.infrastructure.entrypoints.car;

import com.ufinet.carmanager.domain.car.ports.in.CreateCarByUserUseCase;
import com.ufinet.carmanager.domain.car.ports.in.DeleteCarByUserUseCase;
import com.ufinet.carmanager.domain.car.ports.in.GetAllCarsByUserUseCase;
import com.ufinet.carmanager.domain.car.ports.in.UpdateCarByUserUseCase;
import com.ufinet.carmanager.domain.car.ports.in.UpdateCarStatusByUserUseCase;
import com.ufinet.carmanager.infrastructure.config.security.SecurityUtils;
import com.ufinet.carmanager.infrastructure.entrypoints.car.dto.request.CreateCarRequestDTO;
import com.ufinet.carmanager.infrastructure.entrypoints.car.dto.request.UpdateCarRequestDTO;
import com.ufinet.carmanager.infrastructure.entrypoints.car.mapper.CarDTOMapper;
import com.ufinet.carmanager.infrastructure.entrypoints.config.ApiResponse;
import com.ufinet.carmanager.infrastructure.entrypoints.config.validators.ServerRequestValidator;
import com.ufinet.carmanager.infrastructure.entrypoints.exceptions.ServerRequestValidationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@AllArgsConstructor
public class CarHandler {
    private final ServerRequestValidator serverRequestValidator;
    private final GetAllCarsByUserUseCase getAllCarsByUserUseCase;
    private final CreateCarByUserUseCase createCarByUserUseCase;
    private final UpdateCarByUserUseCase updateCarByUserUseCase;
    private final UpdateCarStatusByUserUseCase updateCarStatusByUserUseCase;
    private final DeleteCarByUserUseCase deleteCarByUserUseCase;
    private final CarDTOMapper carDTOMapper;
    private final SecurityUtils securityUtils;

    public Mono<ServerResponse> listenGETCars(ServerRequest serverRequest){

        return securityUtils.extractUserId(serverRequest)
                .switchIfEmpty(Mono.error(new ServerRequestValidationException("User can't be empty")))
                .flatMap(userId ->
                        getAllCarsByUserUseCase.getAllCarsByUserId(userId)
                                .map(carDTOMapper::toGetAllCarsResponseDTO)
                                .collectList()
                )
                .flatMap(responseData ->{
                    HttpStatus httpResponseStatus = HttpStatus.OK;
                    return ServerResponse.status(httpResponseStatus)
                            .contentType(APPLICATION_JSON)
                            .bodyValue(new ApiResponse<>(responseData, httpResponseStatus.value(),"Cars retrieved successfully"));
                });
    }

    public Mono<ServerResponse> listenPOSTCreateCar(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateCarRequestDTO.class)
                .switchIfEmpty(Mono.error(new ServerRequestValidationException("Car info can't be empty")))
                .flatMap(serverRequestValidator::validate)
                .flatMap(createCarRequestDTO ->
                        securityUtils.extractUserId(serverRequest)
                        .flatMap(userId ->
                                createCarByUserUseCase.createCarByUserId(carDTOMapper.toDomain(userId, createCarRequestDTO))
                        )
                        .map(carDTOMapper::toCreateResponseDTO)
                )
                .flatMap(responseData ->{
                    HttpStatus httpResponseStatus = HttpStatus.CREATED;
                    return ServerResponse.status(httpResponseStatus)
                                .contentType(APPLICATION_JSON)
                                .bodyValue(new ApiResponse<>(responseData, httpResponseStatus.value(),"Cars created successfully"));
                });
    }

    public Mono<ServerResponse> listenPUTUpdateCar(ServerRequest serverRequest) {
        Long carId = Long.parseLong(serverRequest.pathVariable("carId"));

        return serverRequest.bodyToMono(UpdateCarRequestDTO.class)
                .switchIfEmpty(Mono.error(new ServerRequestValidationException("Car info can't be empty")))
                .flatMap(serverRequestValidator::validate)
                .flatMap(updateCarRequestDTO ->
                        securityUtils.extractUserId(serverRequest)
                                .flatMap(userId ->
                                        updateCarByUserUseCase.updateCarByUserId(carDTOMapper.toDomain(userId, carId, updateCarRequestDTO))
                                )
                                .map(carDTOMapper::toUpdateResponseDTO)
                )
                .flatMap(responseData ->{
                    HttpStatus httpResponseStatus = HttpStatus.OK;
                    return ServerResponse.status(httpResponseStatus)
                            .contentType(APPLICATION_JSON)
                            .bodyValue(new ApiResponse<>(responseData, httpResponseStatus.value(),"Car updated successfully"));
                });
    }

    public Mono<ServerResponse> listenPATCHUpdateCar(ServerRequest serverRequest) {
        Long carId = Long.parseLong(serverRequest.pathVariable("carId"));

        return securityUtils.extractUserId(serverRequest)
                .flatMap(userId -> updateCarStatusByUserUseCase.updateCarStatus(userId, carId))
                .then(Mono.defer(() ->{
                    HttpStatus httpResponseStatus = HttpStatus.OK;
                    return ServerResponse.status(httpResponseStatus)
                            .contentType(APPLICATION_JSON)
                            .bodyValue(new ApiResponse<>(httpResponseStatus.value(),"Car status updated successfully"));
                }));
    }

    public Mono<ServerResponse> listenDELETECar(ServerRequest serverRequest) {
        Long carId = Long.parseLong(serverRequest.pathVariable("carId"));

        return securityUtils.extractUserId(serverRequest)
                .flatMap(userId -> deleteCarByUserUseCase.deleteCar(userId, carId))
                .then(Mono.defer(() ->{
                    HttpStatus httpResponseStatus = HttpStatus.OK;
                    return ServerResponse.status(httpResponseStatus)
                            .contentType(APPLICATION_JSON)
                            .bodyValue(new ApiResponse<>(httpResponseStatus.value(),"Car deleted successfully"));
                }));
    }
}
