package com.reelnet.infrastructure.persistence.service;

import javax.sql.DataSource;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseConnectionService {
    
    private final DataSource dataSource;

    public boolean isConnected() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(1000);
        } catch (Exception e) {
            log.error("Failed to check database connection", e);
            return false;
        }
    }

    public Map<String, Object> getDatabaseMetadata() {
        Map<String, Object> metadata = new LinkedHashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData dbMetadata = connection.getMetaData();
            metadata.put("databaseProductName", dbMetadata.getDatabaseProductName());
            metadata.put("databaseProductVersion", dbMetadata.getDatabaseProductVersion());
            metadata.put("driverName", dbMetadata.getDriverName());
            metadata.put("driverVersion", dbMetadata.getDriverVersion());
            metadata.put("url", dbMetadata.getURL());
            metadata.put("userName", dbMetadata.getUserName());
            metadata.put("maxConnections", dbMetadata.getMaxConnections());
            metadata.put("defaultTransactionIsolation", getTransactionIsolationName(dbMetadata.getDefaultTransactionIsolation()));
        } catch (Exception e) {
            log.error("Failed to get database metadata", e);
            metadata.put("error", e.getMessage());
        }
        return metadata;
    }

    private String getTransactionIsolationName(int level) {
        return switch (level) {
            case Connection.TRANSACTION_NONE -> "NONE";
            case Connection.TRANSACTION_READ_UNCOMMITTED -> "READ_UNCOMMITTED";
            case Connection.TRANSACTION_READ_COMMITTED -> "READ_COMMITTED";
            case Connection.TRANSACTION_REPEATABLE_READ -> "REPEATABLE_READ";
            case Connection.TRANSACTION_SERIALIZABLE -> "SERIALIZABLE";
            default -> "UNKNOWN";
        };
    }
} 