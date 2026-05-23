package com.ufinet.carmanager.infrastructure.entrypoints.config.validators;

import com.ufinet.carmanager.infrastructure.entrypoints.exceptions.ServerRequestValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class ServerRequestValidator {
    private final Validator validator;

    public <T> Mono<T> validate(T input) {
        return Mono.defer(() -> {
            Set<ConstraintViolation<T>> violations = validator.validate(input);
            if (violations.isEmpty())
                return Mono.just(input);

            List<ServerRequestValidationException.FieldError> errorFields = violations.stream()
                    .map(viol ->
                            new ServerRequestValidationException.FieldError(
                                    viol.getPropertyPath().toString(),
                                    viol.getMessage()
                            )
                    ).toList();

            return Mono.error(new ServerRequestValidationException("Invalid request params", errorFields));
        });
    }
}

