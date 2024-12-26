package com.reelnet;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
    "com.reelnet.features",
    "com.reelnet.core.repositories"
})
@EntityScan(basePackages = {
    "com.reelnet.features.*.domain.model",
    "com.reelnet.core.model"
})
@EnableCaching
@OpenAPIDefinition(
    info = @Info(
        title = "ReelNet API",
        version = "1.0",
        description = "ReelNet REST API Documentation"
    )
)
public class ReelNetApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReelNetApplication.class, args);
    }
}