package com.ufinet.carmanager.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppUrlConfig {
    @Value("${spring.application.base-path}")
    private String BASE_URL;

    public String getBaseUrl() {
        return BASE_URL;
    }
}
