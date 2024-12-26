package com.reelnet.infrastructure.monitoring.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class DiskHealthService {

    public Map<String, Object> checkDiskHealth() {
        Map<String, Object> healthData = new LinkedHashMap<>();
        
        try {
            File[] roots = File.listRoots();
            Map<String, Object> diskMetrics = new LinkedHashMap<>();
            
            for (File root : roots) {
                Map<String, Object> partitionMetrics = new LinkedHashMap<>();
                partitionMetrics.put("totalSpace", bytesToGigabytes(root.getTotalSpace()));
                partitionMetrics.put("freeSpace", bytesToGigabytes(root.getFreeSpace()));
                partitionMetrics.put("usableSpace", bytesToGigabytes(root.getUsableSpace()));
                
                // Calculate usage percentage
                double usagePercentage = ((double) (root.getTotalSpace() - root.getFreeSpace()) / root.getTotalSpace()) * 100;
                partitionMetrics.put("usagePercentage", Math.round(usagePercentage * 100.0) / 100.0);
                
                diskMetrics.put(root.getAbsolutePath(), partitionMetrics);
            }
            
            // Check if any partition is running low on space
            boolean isLowSpace = false;
            for (File root : roots) {
                double freeSpacePercentage = ((double) root.getFreeSpace() / root.getTotalSpace()) * 100;
                if (freeSpacePercentage < 10) { // Alert if less than 10% free space
                    isLowSpace = true;
                    break;
                }
            }
            
            healthData.put("status", isLowSpace ? "WARNING" : "UP");
            healthData.put("partitions", diskMetrics);
            
        } catch (Exception e) {
            log.error("Error checking disk health", e);
            healthData.put("status", "DOWN");
            healthData.put("error", e.getMessage());
        }
        
        return healthData;
    }
    
    private double bytesToGigabytes(long bytes) {
        return Math.round((bytes / (1024.0 * 1024.0 * 1024.0)) * 100.0) / 100.0;
    }
} 