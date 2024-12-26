package com.reelnet.infrastructure.monitoring.service;

import com.reelnet.infrastructure.monitoring.config.MonitoringConstants;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheHealthService {
    
    private final CacheManager cacheManager;
    
    public Map<String, Object> checkCacheHealth() {
        Map<String, Object> healthData = new LinkedHashMap<>();
        try {
            Collection<String> cacheNames = cacheManager.getCacheNames();
            Map<String, Object> cacheMetrics = new LinkedHashMap<>();
            
            for (String cacheName : cacheNames) {
                Cache cache = cacheManager.getCache(cacheName);
                if (cache != null) {
                    Map<String, Object> cacheStats = new LinkedHashMap<>();
                    cacheStats.put("name", cacheName);
                    cacheStats.put("status", MonitoringConstants.STATUS_UP);
                    cacheMetrics.put(cacheName, cacheStats);
                }
            }
            
            healthData.put("status", MonitoringConstants.STATUS_UP);
            healthData.put("caches", cacheMetrics);
            healthData.put("message", "Cache is functioning normally");
            
        } catch (Exception e) {
            log.error("Error checking cache health", e);
            healthData.put("status", MonitoringConstants.STATUS_DOWN);
            healthData.put("error", e.getMessage());
        }
        return healthData;
    }
} 