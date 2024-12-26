package com.reelnet.infrastructure.monitoring.health;

import com.reelnet.infrastructure.monitoring.service.DatabaseHealthService;
import com.reelnet.shared.dto.ApiResponse;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DatabaseHealthService databaseHealthService;

    @Override
    public Health health() {
        ApiResponse<Map<String, Object>> healthData = databaseHealthService.checkDatabaseHealth();
        String status = (String) healthData.getData().get("status");

        return "UP".equals(status)
                ? Health.up().withDetails(healthData.getData()).build()
                : Health.down().withDetails(healthData.getData()).build();
    }
}