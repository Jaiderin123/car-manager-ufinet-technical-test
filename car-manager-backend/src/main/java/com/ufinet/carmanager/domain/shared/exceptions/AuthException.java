package com.ufinet.carmanager.domain.shared.exceptions;

public class AuthException extends RuntimeException {

    public AuthException() {
        super("Invalid credentials");
    }

    public AuthException(String message) {
        super(message);
    }
}
