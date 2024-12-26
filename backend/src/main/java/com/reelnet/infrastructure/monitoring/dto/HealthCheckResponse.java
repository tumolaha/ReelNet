package com.reelnet.infrastructure.monitoring.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckResponse {
    private String status;
    private LocalDateTime timestamp;
    private Map<String, Object> details;
    private Map<String, Object> components;
    private String version;
    private Map<String, Object> error;

    public HealthCheckResponse(String status, Map<String, Object> details) {
        this.status = status;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
} 