package com.reelnet.infrastructure.monitoring.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class HealthCheckException extends RuntimeException {
    
    private final String code;
    private final HttpStatus status;
    private final Object details;

    public HealthCheckException(String message, String code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
        this.details = null;
    }

    public HealthCheckException(String message, String code, HttpStatus status, Object details) {
        super(message);
        this.code = code;
        this.status = status;
        this.details = details;
    }

    public static HealthCheckException databaseError(String message) {
        return new HealthCheckException(
            message,
            "DATABASE_ERROR",
            HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    public static HealthCheckException systemError(String message) {
        return new HealthCheckException(
            message,
            "SYSTEM_ERROR",
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    public static HealthCheckException diskError(String message) {
        return new HealthCheckException(
            message,
            "DISK_ERROR",
            HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    public static HealthCheckException cacheError(String message) {
        return new HealthCheckException(
            message,
            "CACHE_ERROR",
            HttpStatus.SERVICE_UNAVAILABLE
        );
    }

    public static HealthCheckException jvmError(String message) {
        return new HealthCheckException(
            message,
            "JVM_ERROR",
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    public static HealthCheckException configurationError(String message) {
        return new HealthCheckException(
            message,
            "CONFIGURATION_ERROR",
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    public static HealthCheckException authenticationError(String message) {
        return new HealthCheckException(
            message,
            "AUTHENTICATION_ERROR",
            HttpStatus.UNAUTHORIZED
        );
    }
} 