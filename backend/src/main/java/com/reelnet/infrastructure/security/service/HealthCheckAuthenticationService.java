package com.reelnet.infrastructure.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
@Service
public class HealthCheckAuthenticationService {

    private static final String MONITORING_ROLE = "ROLE_MONITORING";

    @Value("${health.check.api-key}")
    private String configuredApiKey;

    @Value("${health.check.enabled:true}")
    private boolean healthCheckEnabled;

    public boolean authenticateForHealthCheck(String providedApiKey) {
        if (!healthCheckEnabled) {
            log.warn("Health check authentication is disabled");
            return false;
        }

        if (providedApiKey == null || providedApiKey.isEmpty()) {
            log.warn("No API key provided for health check");
            return false;
        }

        if (isValidHealthCheckApiKey(providedApiKey)) {
            Authentication auth = new UsernamePasswordAuthenticationToken(
                "health-check-service",
                null,
                Collections.singletonList(new SimpleGrantedAuthority(MONITORING_ROLE))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.debug("Health check authentication successful");
            return true;
        }

        log.warn("Invalid API key provided for health check");
        return false;
    }

    private boolean isValidHealthCheckApiKey(String providedApiKey) {
        return configuredApiKey != null && 
               !configuredApiKey.isEmpty() && 
               configuredApiKey.equals(providedApiKey);
    }
} 