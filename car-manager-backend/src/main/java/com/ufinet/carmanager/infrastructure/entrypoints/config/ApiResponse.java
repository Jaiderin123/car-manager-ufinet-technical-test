package com.ufinet.carmanager.infrastructure.entrypoints.config;

import java.time.LocalDateTime;

public record ApiResponse<T>(
        T data,
        int status,
        String message,
        LocalDateTime timestamp
) {
    public ApiResponse(T data, int status, String message) {
        this(data, status, message, LocalDateTime.now());
    }

    public ApiResponse(int status, String message) {
        this(null, status, message, LocalDateTime.now());
    }
}
