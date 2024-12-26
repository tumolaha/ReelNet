package com.reelnet.infrastructure.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reelnet.infrastructure.monitoring.service.HealthCheckService;
import com.reelnet.infrastructure.security.service.HealthCheckAuthenticationService;
import com.reelnet.shared.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HealthCheckAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final HealthCheckAuthenticationService authenticationService;
    private final HealthCheckService healthCheckService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        
        response.setContentType("application/json");
        String apiKey = request.getHeader("X-API-KEY");
        String requestUri = request.getRequestURI();

        // Handle health check endpoints
        if (isHealthCheckEndpoint(requestUri)) {
            handleHealthCheckEndpoint(request, response, apiKey);
            return;
        }

        // Handle other unauthorized requests
        handleUnauthorizedRequest(response, requestUri, authException);
    }

    private boolean isHealthCheckEndpoint(String uri) {
        return uri.startsWith("/api/health") || uri.startsWith("/actuator");
    }

    private void handleHealthCheckEndpoint(HttpServletRequest request, HttpServletResponse response, String apiKey) 
            throws IOException {
        try {
            if (authenticationService.authenticateForHealthCheck(apiKey)) {
                // If authenticated, process health check
                processHealthCheck(request, response);
            } else {
                // If not authenticated, return unauthorized
                sendUnauthorizedResponse(response, request.getRequestURI());
            }
        } catch (Exception e) {
            log.error("Error processing health check request", e);
            sendErrorResponse(response, e);
        }
    }

    private void processHealthCheck(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        String requestUri = request.getRequestURI();
        
        if (requestUri.endsWith("/database")) {
            sendResponse(response, healthCheckService.checkDatabaseHealth());
        } else if (requestUri.endsWith("/system")) {
            sendResponse(response, healthCheckService.checkSystemHealth());
        } else if (requestUri.endsWith("/disk")) {
            sendResponse(response, healthCheckService.checkDiskHealth());
        } else {
            // Forward to the actual endpoint for other health check paths
            try {
                request.getRequestDispatcher(requestUri).forward(request, response);
            } catch (Exception e) {
                log.error("Error forwarding request", e);
                sendErrorResponse(response, e);
            }
        }
    }

    private void sendResponse(HttpServletResponse response, Map<String, Object> healthData) 
            throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        ApiResponse<Map<String, Object>> apiResponse = ApiResponse.success(healthData);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String path) 
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>error(
            "Invalid or missing API key",
            "UNAUTHORIZED"
        );
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private void sendErrorResponse(HttpServletResponse response, Exception e) 
            throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>error(
            "Error processing health check request: " + e.getMessage(),
            "INTERNAL_SERVER_ERROR"
        );
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private void handleUnauthorizedRequest(HttpServletResponse response, String path, 
            AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>error(
            authException.getMessage(),
            "UNAUTHORIZED"
        );
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}