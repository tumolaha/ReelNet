package com.reelnet.infrastructure.monitoring.aspect;

import com.reelnet.infrastructure.monitoring.metric.ApiMetrics;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class MetricsAspect {

    private final ApiMetrics apiMetrics;

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object measureApiPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        Timer.Sample timer = apiMetrics.startTimer();
        apiMetrics.incrementTotalRequests();
        
        try {
            Object result = joinPoint.proceed();
            timer.stop(apiMetrics.getResponseTimeTimer());
            return result;
        } catch (Exception e) {
            apiMetrics.incrementErrorRequests();
            timer.stop(apiMetrics.getResponseTimeTimer());
            throw e;
        }
    }
} 