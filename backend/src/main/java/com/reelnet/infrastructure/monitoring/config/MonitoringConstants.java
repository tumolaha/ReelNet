package com.reelnet.infrastructure.monitoring.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MonitoringConstants {
    public static final String STATUS_UP = "UP";
    public static final String STATUS_DOWN = "DOWN";
    public static final String STATUS_WARNING = "WARNING";
    
    public static final double DISK_SPACE_WARNING_THRESHOLD = 10.0; // 10%
    public static final int CONNECTION_TIMEOUT = 1000; // 1 second
    
    public static final String ERROR_CODE_DB_CONNECTION = "DB_CONNECTION_ERROR";
    public static final String ERROR_CODE_DB_CHECK = "DB_CHECK_ERROR";
    public static final String ERROR_CODE_SYSTEM_CHECK = "SYSTEM_CHECK_ERROR";
    public static final String ERROR_CODE_DISK_CHECK = "DISK_CHECK_ERROR";
    public static final String ERROR_CODE_DISK_SPACE = "DISK_SPACE_CRITICAL";
    public static final String ERROR_CODE_SYSTEM_PARTIALLY_DOWN = "SYSTEM_PARTIALLY_DOWN";
    public static final String ERROR_CODE_HEALTH_CHECK = "HEALTH_CHECK_ERROR";
    
    public static final String[] HEALTH_ENDPOINTS = {
        "/api/health/**",
        "/actuator/**"
    };
} 