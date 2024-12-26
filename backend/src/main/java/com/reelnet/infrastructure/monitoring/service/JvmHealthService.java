package com.reelnet.infrastructure.monitoring.service;

import com.reelnet.infrastructure.monitoring.config.MonitoringConstants;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JvmHealthService {

    public Map<String, Object> checkJvmHealth() {
        Map<String, Object> healthData = new LinkedHashMap<>();
        try {
            healthData.put("status", MonitoringConstants.STATUS_UP);
            healthData.put("heap", getHeapMemoryUsage());
            healthData.put("nonHeap", getNonHeapMemoryUsage());
            healthData.put("threads", getThreadMetrics());
            healthData.put("gc", getGarbageCollectionMetrics());
            healthData.put("runtime", getRuntimeMetrics());
            
        } catch (Exception e) {
            log.error("Error checking JVM health", e);
            healthData.put("status", MonitoringConstants.STATUS_DOWN);
            healthData.put("error", e.getMessage());
        }
        return healthData;
    }

    private Map<String, Object> getHeapMemoryUsage() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        Map<String, Object> heapMetrics = new LinkedHashMap<>();
        
        heapMetrics.put("used", memoryBean.getHeapMemoryUsage().getUsed());
        heapMetrics.put("committed", memoryBean.getHeapMemoryUsage().getCommitted());
        heapMetrics.put("max", memoryBean.getHeapMemoryUsage().getMax());
        heapMetrics.put("init", memoryBean.getHeapMemoryUsage().getInit());
        
        // Calculate usage percentage
        double usagePercentage = ((double) memoryBean.getHeapMemoryUsage().getUsed() / 
                                 memoryBean.getHeapMemoryUsage().getMax()) * 100;
        heapMetrics.put("usagePercentage", Math.round(usagePercentage * 100.0) / 100.0);
        
        return heapMetrics;
    }

    private Map<String, Object> getNonHeapMemoryUsage() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        Map<String, Object> nonHeapMetrics = new LinkedHashMap<>();
        
        nonHeapMetrics.put("used", memoryBean.getNonHeapMemoryUsage().getUsed());
        nonHeapMetrics.put("committed", memoryBean.getNonHeapMemoryUsage().getCommitted());
        nonHeapMetrics.put("max", memoryBean.getNonHeapMemoryUsage().getMax());
        nonHeapMetrics.put("init", memoryBean.getNonHeapMemoryUsage().getInit());
        
        return nonHeapMetrics;
    }

    private Map<String, Object> getThreadMetrics() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        Map<String, Object> threadMetrics = new LinkedHashMap<>();
        
        threadMetrics.put("threadCount", threadBean.getThreadCount());
        threadMetrics.put("peakThreadCount", threadBean.getPeakThreadCount());
        threadMetrics.put("totalStartedThreadCount", threadBean.getTotalStartedThreadCount());
        threadMetrics.put("daemonThreadCount", threadBean.getDaemonThreadCount());
        threadMetrics.put("deadlockedThreads", threadBean.findDeadlockedThreads() != null ? 
                         threadBean.findDeadlockedThreads().length : 0);
        
        return threadMetrics;
    }

    private Map<String, Object> getGarbageCollectionMetrics() {
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        Map<String, Object> gcMetrics = new LinkedHashMap<>();
        
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            Map<String, Object> collectorMetrics = new LinkedHashMap<>();
            collectorMetrics.put("collectionCount", gcBean.getCollectionCount());
            collectorMetrics.put("collectionTime", gcBean.getCollectionTime());
            collectorMetrics.put("memoryPoolNames", gcBean.getMemoryPoolNames());
            
            gcMetrics.put(gcBean.getName(), collectorMetrics);
        }
        
        return gcMetrics;
    }

    private Map<String, Object> getRuntimeMetrics() {
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> runtimeMetrics = new LinkedHashMap<>();
        
        runtimeMetrics.put("availableProcessors", runtime.availableProcessors());
        runtimeMetrics.put("freeMemory", runtime.freeMemory());
        runtimeMetrics.put("totalMemory", runtime.totalMemory());
        runtimeMetrics.put("maxMemory", runtime.maxMemory());
        
        // JVM uptime
        runtimeMetrics.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime());
        runtimeMetrics.put("startTime", ManagementFactory.getRuntimeMXBean().getStartTime());
        
        // System properties
        Map<String, String> systemProps = new LinkedHashMap<>();
        systemProps.put("java.version", System.getProperty("java.version"));
        systemProps.put("java.vm.name", System.getProperty("java.vm.name"));
        systemProps.put("java.vm.version", System.getProperty("java.vm.version"));
        systemProps.put("java.vm.vendor", System.getProperty("java.vm.vendor"));
        runtimeMetrics.put("systemProperties", systemProps);
        
        return runtimeMetrics;
    }
} 