package com.ufinet.carmanager.infrastructure.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    private List<String> publicPaths = List.of();
    private Jwt jwt = new Jwt();

    @Getter
    @Setter
    public static class Jwt {
        private String secret;
    }
}