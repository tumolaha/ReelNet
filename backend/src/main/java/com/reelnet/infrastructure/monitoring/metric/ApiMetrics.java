package com.reelnet.infrastructure.monitoring.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;
import lombok.Getter;

@Component
@Getter
public class ApiMetrics {
    private final Counter totalRequestsCounter;
    private final Counter errorRequestsCounter;
    private final Timer responseTimeTimer;

    public ApiMetrics(MeterRegistry registry) {
        this.totalRequestsCounter = Counter.builder("api.requests.total")
                .description("Total number of API requests")
                .register(registry);
                
        this.errorRequestsCounter = Counter.builder("api.requests.errors")
                .description("Total number of API errors")
                .register(registry);
                
        this.responseTimeTimer = Timer.builder("api.response.time")
                .description("API response time")
                .register(registry);
    }

    public void incrementTotalRequests() {
        totalRequestsCounter.increment();
    }

    public void incrementErrorRequests() {
        errorRequestsCounter.increment();
    }

    public Timer.Sample startTimer() {
        return Timer.start();
    }

    public void stopTimer(Timer.Sample sample) {
        sample.stop(responseTimeTimer);
    }
} 