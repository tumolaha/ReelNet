package com.reelnet.infrastructure.monitoring.filter;

import com.reelnet.infrastructure.monitoring.metric.CustomMetrics;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {

    private final CustomMetrics customMetrics;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        String requestId = UUID.randomUUID().toString();
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        long startTime = Instant.now().toEpochMilli();
        
        // Set MDC context
        try {
            setupMDC(request, requestId);
            
            // Log request
            logRequest(request);
            
            // Process request
            filterChain.doFilter(request, response);
            
            // Record metrics
            long duration = Instant.now().toEpochMilli() - startTime;
            customMetrics.recordApiLatency(requestUri, method, duration);
            
            // Log response
            logResponse(response, duration);
            
        } catch (Exception e) {
            log.error("Error processing request: {}", e.getMessage());
            customMetrics.incrementApiErrors(requestUri, "INTERNAL_SERVER_ERROR");
            throw e;
        } finally {
            MDC.clear();
        }
    }

    private void setupMDC(HttpServletRequest request, String requestId) {
        MDC.put("requestId", requestId);
        MDC.put("method", request.getMethod());
        MDC.put("uri", request.getRequestURI());
        MDC.put("clientIp", getClientIp(request));
        MDC.put("userAgent", request.getHeader("User-Agent"));
        
        String username = request.getUserPrincipal() != null ? 
                request.getUserPrincipal().getName() : "anonymous";
        MDC.put("username", username);
    }

    private void logRequest(HttpServletRequest request) {
        log.info("Incoming Request [{}] {} from IP: {}, User-Agent: {}",
                request.getMethod(),
                request.getRequestURI(),
                getClientIp(request),
                request.getHeader("User-Agent"));
    }

    private void logResponse(HttpServletResponse response, long duration) {
        log.info("Response Status: {}, Duration: {}ms",
                response.getStatus(),
                duration);
    }

    private String getClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
} 