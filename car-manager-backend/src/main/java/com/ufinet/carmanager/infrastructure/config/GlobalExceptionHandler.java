package com.ufinet.carmanager.infrastructure.config;

import com.ufinet.carmanager.domain.shared.exceptions.AuthException;
import com.ufinet.carmanager.domain.shared.exceptions.BusinessException;
import com.ufinet.carmanager.infrastructure.entrypoints.config.ApiResponse;
import com.ufinet.carmanager.infrastructure.entrypoints.exceptions.ServerRequestValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@Order(-2)
@Component
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  WebProperties webProperties,
                                  ApplicationContext applicationContext,
                                  ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageReaders(serverCodecConfigurer.getReaders());
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::customErrorResponse);
    }

    private Mono<ServerResponse> customErrorResponse(ServerRequest request) {
        Throwable error = getError(request);
        HttpStatus status = resolveHttpStatus(error);
        Object data = resolveData(error);
        String message = resolveMessage(error, status);

        if (status == HttpStatus.INTERNAL_SERVER_ERROR)
            log.error("Unexpected error: {}", error.getMessage(), error);
        else
            log.warn("Handled exception [{}]: {}", error.getClass().getSimpleName(), error.getMessage());

        return ServerResponse.status(status)
                .contentType(APPLICATION_JSON)
                .bodyValue(buildResponse(data, status.value(), message));
    }

    private HttpStatus resolveHttpStatus(Throwable error) {
        return switch (error) {
            case BusinessException be -> HttpStatus.BAD_REQUEST;
            case ServerRequestValidationException sve -> HttpStatus.BAD_REQUEST;
            case ServerWebInputException swie -> HttpStatus.BAD_REQUEST;
            case DataIntegrityViolationException dive -> HttpStatus.CONFLICT;
            case NoResourceFoundException nrfe -> HttpStatus.NOT_FOUND;
            case AuthException ae -> HttpStatus.UNAUTHORIZED;
            case null, default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    private String resolveMessage(Throwable error, HttpStatus status) {
        return switch (error) {
            case ServerWebInputException swie -> "Data request format invalid";
            case DataIntegrityViolationException dive -> resolveDataIntegrityMessage(dive);
            case BusinessException be -> be.getMessage();
            case AuthException ae -> ae.getMessage();
            case ServerRequestValidationException srve -> srve.getMessage();
            default -> status == HttpStatus.INTERNAL_SERVER_ERROR
                    ? "Internal server error, please try again later"
                    : error.getMessage();
        };
    }

    private Object resolveData(Throwable error) {
        return switch (error) {
            case ServerRequestValidationException srve -> !srve.getErrors().isEmpty() ? srve.getErrors() : null;
            case ServerWebInputException swie -> swie.getCause() != null ? swie.getCause().getLocalizedMessage() : swie.getReason();
            default -> null;
        };
    }

    private String resolveDataIntegrityMessage(DataIntegrityViolationException ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : "";
        if (msg.contains("UQ_cars_plate")) return "Plate already registered";
        if (msg.contains("UQ_users_email")) return "Email already registered";
        return "Data integrity violation";
    }

    private <T> ApiResponse<T> buildResponse(T data, int status, String message) {
        return new ApiResponse<>(data, status, message);
    }
}