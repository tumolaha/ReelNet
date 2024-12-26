package com.reelnet.infrastructure.monitoring.service;

import com.reelnet.infrastructure.persistence.service.DatabaseConnectionService;
import com.reelnet.shared.dto.ApiResponse;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseHealthService {
    
    private final DatabaseConnectionService databaseConnectionService;

    public ApiResponse<Map<String, Object>> checkDatabaseHealth() {
        try {
            boolean isConnected = databaseConnectionService.isConnected();
            if (!isConnected) {
                return ApiResponse.<Map<String, Object>>error(
                    "Database connection failed",
                    "DB_CONNECTION_ERROR"
                );
            }

            Map<String, Object> healthData = new LinkedHashMap<>();
            healthData.put("status", "UP");
            healthData.put("message", "Database is responding normally");
            
            // Add database metadata
            Map<String, Object> metadata = databaseConnectionService.getDatabaseMetadata();
            healthData.put("metadata", metadata);
            
            // Add connection details
            Map<String, Object> connectionDetails = new LinkedHashMap<>();
            connectionDetails.put("database", metadata.get("databaseProductName"));
            connectionDetails.put("version", metadata.get("databaseProductVersion"));
            connectionDetails.put("driver", metadata.get("driverName"));
            connectionDetails.put("url", metadata.get("url"));
            connectionDetails.put("maxConnections", metadata.get("maxConnections"));
            connectionDetails.put("transactionIsolation", metadata.get("defaultTransactionIsolation"));
            healthData.put("connection", connectionDetails);

            return ApiResponse.success(
                healthData,
                "Database health check completed successfully"
            );
            
        } catch (Exception e) {
            log.error("Error checking database health", e);
            return ApiResponse.<Map<String, Object>>error(
                "Error checking database health: " + e.getMessage(),
                "DB_CHECK_ERROR"
            );
        }
    }
} 