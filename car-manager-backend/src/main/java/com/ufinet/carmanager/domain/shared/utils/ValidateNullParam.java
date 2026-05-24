package com.ufinet.carmanager.domain.shared.utils;

public final class ValidateNullParam {
    private ValidateNullParam() {}

    public static <T> T orDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
