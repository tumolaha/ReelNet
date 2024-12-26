package com.reelnet.infrastructure.monitoring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HealthCheckService {
    
    private final DatabaseHealthService databaseHealthService;
    private final SystemHealthService systemHealthService;
    private final DiskHealthService diskHealthService;

    public Map<String, Object> checkDatabaseHealth() {
        return databaseHealthService.checkDatabaseHealth().getData();
    }

    public Map<String, Object> checkSystemHealth() {
        return systemHealthService.checkSystemHealth();
    }

    public Map<String, Object> checkDiskHealth() {
        return diskHealthService.checkDiskHealth();
    }
} 