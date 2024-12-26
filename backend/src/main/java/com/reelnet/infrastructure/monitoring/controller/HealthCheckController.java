package com.reelnet.infrastructure.monitoring.controller;

import com.reelnet.core.shared.BaseController;
import com.reelnet.infrastructure.monitoring.service.DatabaseHealthService;
import com.reelnet.infrastructure.monitoring.service.DiskHealthService;
import com.reelnet.infrastructure.monitoring.service.SystemHealthService;
import com.reelnet.shared.dto.ApiResponse;
import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.CompositeHealth;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.ExternalDocumentation;

import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
@Tag(name = "Health Check", description = "Endpoints for system health monitoring")
@SecurityRequirement(name = "api-key")
public class HealthCheckController extends BaseController {

    private final HealthEndpoint healthEndpoint;
    private final DatabaseHealthService databaseHealthService;
    private final SystemHealthService systemHealthService;
    private final DiskHealthService diskHealthService;

    @GetMapping
    @Operation(summary = "Get overall system health status", description = "Retrieves the health status of all system components including database, system resources, and disk space.", externalDocs = @ExternalDocumentation(description = "Health Check Documentation", url = "https://reelnet.com/docs/health-check"))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "System health check completed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "One or more system components are down", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing API key", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkHealth() {
        try {
            HealthComponent health = healthEndpoint.health();
            Map<String, Object> allHealthData = new LinkedHashMap<>();

            // Add basic health status
            allHealthData.put("status", health.getStatus().getCode());

            // Add health details if available
            if (health instanceof CompositeHealth) {
                CompositeHealth compositeHealth = (CompositeHealth) health;
                allHealthData.putAll(compositeHealth.getComponents());
            }

            // Add detailed health data with error handling
            try {
                allHealthData.put("database", databaseHealthService.checkDatabaseHealth().getData());
            } catch (Exception e) {
                log.error("Error checking database health", e);
                Map<String, Object> dbError = new LinkedHashMap<>();
                dbError.put("status", "DOWN");
                dbError.put("error", getDetailedErrorMessage(e));
                allHealthData.put("database", dbError);
            }

            try {
                allHealthData.put("system", systemHealthService.checkSystemHealth());
            } catch (Exception e) {
                log.error("Error checking system health", e);
                Map<String, Object> sysError = new LinkedHashMap<>();
                sysError.put("status", "DOWN");
                sysError.put("error", getDetailedErrorMessage(e));
                allHealthData.put("system", sysError);
            }

            try {
                allHealthData.put("disk", diskHealthService.checkDiskHealth());
            } catch (Exception e) {
                log.error("Error checking disk health", e);
                Map<String, Object> diskError = new LinkedHashMap<>();
                diskError.put("status", "DOWN");
                diskError.put("error", getDetailedErrorMessage(e));
                allHealthData.put("disk", diskError);
            }

            // Determine overall status
            String overallStatus = determineOverallStatus(allHealthData);
            allHealthData.put("status", overallStatus);

            if ("DOWN".equals(overallStatus)) {
                return error("One or more system components are down",
                        "SYSTEM_PARTIALLY_DOWN",
                        HttpStatus.SERVICE_UNAVAILABLE,
                        allHealthData);
            }

            return success(allHealthData, "System health check completed successfully");
        } catch (Exception e) {
            log.error("Error performing health check", e);
            return error("Error checking system health: " + getDetailedErrorMessage(e),
                    "HEALTH_CHECK_ERROR",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/database")
    @Operation(summary = "Check database health status", description = "Checks the health of the database connection and returns detailed database metrics.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Database health check completed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "Database is not available", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkDatabaseHealth() {
        try {
            ApiResponse<Map<String, Object>> response = databaseHealthService.checkDatabaseHealth();
            if ("ERROR".equals(response.getStatus())) {
                return error(response.getError().getMessage(),
                        response.getError().getCode(),
                        HttpStatus.SERVICE_UNAVAILABLE);
            }
            return success(response.getData(), "Database health check completed successfully");
        } catch (Exception e) {
            log.error("Error checking database health", e);
            String errorMessage = getDetailedErrorMessage(e);
            return error("Database health check failed: " + errorMessage,
                    "DATABASE_CONNECTION_ERROR",
                    HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/system")
    @Operation(summary = "Check system resources health", description = "Monitors system resources including memory, CPU, and threads.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "System resources check completed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error checking system resources", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkSystemHealth() {
        try {
            Map<String, Object> healthData = systemHealthService.checkSystemHealth();
            return success(healthData, "System resources check completed successfully");
        } catch (Exception e) {
            log.error("Error checking system resources", e);
            return error("Error checking system resources: " + getDetailedErrorMessage(e),
                    "SYSTEM_CHECK_ERROR",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/disk")
    @Operation(summary = "Check disk space health", description = "Monitors disk space usage and returns warnings if space is running low.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Disk health check completed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "Disk space critical", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkDiskHealth() {
        try {
            Map<String, Object> healthData = diskHealthService.checkDiskHealth();
            String status = (String) healthData.get("status");
            if ("WARNING".equals(status)) {
                return success(healthData, "Disk space is running low");
            } else if ("DOWN".equals(status)) {
                return error("Disk space critical",
                        "DISK_SPACE_CRITICAL",
                        HttpStatus.SERVICE_UNAVAILABLE);
            }
            return success(healthData, "Disk health check completed successfully");
        } catch (Exception e) {
            log.error("Error checking disk health", e);
            return error("Error checking disk health: " + getDetailedErrorMessage(e),
                    "DISK_CHECK_ERROR",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String determineOverallStatus(Map<String, Object> healthData) {
        boolean hasDown = false;
        boolean hasWarning = false;

        for (Object value : healthData.values()) {
            if (value instanceof Map) {
                String status = (String) ((Map<?, ?>) value).get("status");
                if ("DOWN".equals(status)) {
                    hasDown = true;
                } else if ("WARNING".equals(status)) {
                    hasWarning = true;
                }
            }
        }

        if (hasDown)
            return "DOWN";
        if (hasWarning)
            return "WARNING";
        return "UP";
    }

    private String getDetailedErrorMessage(Exception e) {
        if (e.getCause() instanceof UnknownHostException) {
            return "Database connection failed: Unable to resolve host. Please check database configuration and network connectivity.";
        }

        Throwable rootCause = getRootCause(e);
        return rootCause.getMessage() != null ? rootCause.getMessage() : e.getMessage();
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable rootCause = throwable;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

    protected <T> ResponseEntity<ApiResponse<T>> error(String message, String code, HttpStatus status,
            Map<String, Object> details) {
        return ResponseEntity.status(status)
                .body(ApiResponse.<T>builder()
                        .status("ERROR")
                        .error(ApiResponse.ErrorDetails.builder()
                                .message(message)
                                .code(code)
                                .details(details)
                                .build())
                        .meta(createMetaData())
                        .build());
    }
} 