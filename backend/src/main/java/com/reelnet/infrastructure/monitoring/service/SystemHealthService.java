package com.reelnet.infrastructure.monitoring.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class SystemHealthService {

    public Map<String, Object> checkSystemHealth() {
        Map<String, Object> healthData = new LinkedHashMap<>();
        
        try {
            // Memory metrics
            MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
            Map<String, Object> memoryMetrics = new LinkedHashMap<>();
            memoryMetrics.put("heapMemoryUsage", memoryBean.getHeapMemoryUsage());
            memoryMetrics.put("nonHeapMemoryUsage", memoryBean.getNonHeapMemoryUsage());
            memoryMetrics.put("freeMemory", Runtime.getRuntime().freeMemory());
            memoryMetrics.put("totalMemory", Runtime.getRuntime().totalMemory());
            memoryMetrics.put("maxMemory", Runtime.getRuntime().maxMemory());

            // Thread metrics
            ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
            Map<String, Object> threadMetrics = new LinkedHashMap<>();
            threadMetrics.put("threadCount", threadBean.getThreadCount());
            threadMetrics.put("peakThreadCount", threadBean.getPeakThreadCount());
            threadMetrics.put("totalStartedThreadCount", threadBean.getTotalStartedThreadCount());
            threadMetrics.put("daemonThreadCount", threadBean.getDaemonThreadCount());

            // System metrics
            Map<String, Object> systemMetrics = new LinkedHashMap<>();
            systemMetrics.put("availableProcessors", Runtime.getRuntime().availableProcessors());
            systemMetrics.put("systemLoad", ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
            systemMetrics.put("javaVersion", System.getProperty("java.version"));
            systemMetrics.put("javaVendor", System.getProperty("java.vendor"));
            systemMetrics.put("osName", System.getProperty("os.name"));
            systemMetrics.put("osVersion", System.getProperty("os.version"));
            systemMetrics.put("osArch", System.getProperty("os.arch"));

            healthData.put("status", "UP");
            healthData.put("memory", memoryMetrics);
            healthData.put("thread", threadMetrics);
            healthData.put("system", systemMetrics);
            
        } catch (Exception e) {
            log.error("Error checking system health", e);
            healthData.put("status", "DOWN");
            healthData.put("error", e.getMessage());
        }
        
        return healthData;
    }
}