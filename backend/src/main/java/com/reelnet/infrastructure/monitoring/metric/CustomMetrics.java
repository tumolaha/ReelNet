package com.reelnet.infrastructure.monitoring.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CustomMetrics {
    
    private final MeterRegistry registry;
    
    // Database metrics
    public void recordDatabaseQueryTime(String query, long timeInMs) {
        Timer.builder("db.query.time")
             .tag("query", query)
             .description("Database query execution time")
             .register(registry)
             .record(timeInMs, TimeUnit.MILLISECONDS);
    }
    
    public void incrementDatabaseConnections() {
        Counter.builder("db.connections.active")
              .description("Number of active database connections")
              .register(registry)
              .increment();
    }
    
    // Security metrics
    public void incrementFailedLogin(String username) {
        Counter.builder("security.login.failed")
              .tag("username", username)
              .description("Number of failed login attempts")
              .register(registry)
              .increment();
    }
    
    public void recordAuthenticationTime(long timeInMs) {
        Timer.builder("security.authentication.time")
             .description("Authentication processing time")
             .register(registry)
             .record(timeInMs, TimeUnit.MILLISECONDS);
    }
    
    // Cache metrics
    public void recordCacheHit(String cacheName) {
        Counter.builder("cache.hits")
              .tag("cache", cacheName)
              .description("Number of cache hits")
              .register(registry)
              .increment();
    }
    
    public void recordCacheMiss(String cacheName) {
        Counter.builder("cache.misses")
              .tag("cache", cacheName)
              .description("Number of cache misses")
              .register(registry)
              .increment();
    }
    
    // API metrics
    public void recordApiLatency(String endpoint, String method, long timeInMs) {
        Timer.builder("api.latency")
             .tag("endpoint", endpoint)
             .tag("method", method)
             .description("API endpoint latency")
             .register(registry)
             .record(timeInMs, TimeUnit.MILLISECONDS);
    }
    
    public void incrementApiErrors(String endpoint, String errorCode) {
        Counter.builder("api.errors")
              .tag("endpoint", endpoint)
              .tag("error", errorCode)
              .description("Number of API errors")
              .register(registry)
              .increment();
    }
    
    // Business metrics
    public void recordBusinessOperation(String operation, long timeInMs) {
        Timer.builder("business.operation.time")
             .tag("operation", operation)
             .description("Business operation execution time")
             .register(registry)
             .record(timeInMs, TimeUnit.MILLISECONDS);
    }
    
    public void incrementBusinessEvent(String event) {
        Counter.builder("business.events")
              .tag("event", event)
              .description("Number of business events")
              .register(registry)
              .increment();
    }
} 