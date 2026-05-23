package com.ufinet.carmanager.infrastructure.entrypoints.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ServerRequestValidationException extends RuntimeException{
    //private final String code;
    private final List<FieldError> errors;

    public ServerRequestValidationException(String message/*, String code,*/) {
        super(message);
        //this.code = code;
        this.errors = new ArrayList<>();
    }

    public ServerRequestValidationException(String message, /*String code,*/ List<FieldError> errors) {
        super(message);
        //this.code = code;
        this.errors = errors != null ? errors : new ArrayList<>();
    }

    public record FieldError(
            String field,
            String message
    ) {}
}

