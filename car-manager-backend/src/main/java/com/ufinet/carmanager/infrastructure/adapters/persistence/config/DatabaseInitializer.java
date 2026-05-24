package com.ufinet.carmanager.infrastructure.adapters.persistence.config;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner checkDatabaseConnection(ConnectionFactory connectionFactory) {
        return args -> {
            log.info("Init Sql Server (R2DBC) connection...");

            Mono.from(connectionFactory.create())
                    .flatMap(connection ->
                            Mono.from(connection.createStatement("SELECT 1").execute())
                                    .flatMap(result -> Mono.from(result.map((row, metadata) -> row.get(0))))
                                    .doOnTerminate(() -> Mono.from(connection.close()).subscribe())
                    )
                    .timeout(Duration.ofSeconds(5))
                    .block();

            log.info("¡Sql Server (R2DBC) connection established!");
        };
    }
}
