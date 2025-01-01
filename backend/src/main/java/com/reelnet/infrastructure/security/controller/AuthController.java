package com.reelnet.infrastructure.security.controller;

import com.reelnet.infrastructure.security.service.Auth0AuthenticationService;
import com.reelnet.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final Auth0AuthenticationService auth0Service;

    @GetMapping("/user")
    public ApiResponse<Map<String, Object>> getUserInfo(@AuthenticationPrincipal Jwt principal) {
        String token = principal.getTokenValue();
        return ApiResponse.success(auth0Service.getUserInfo(token));
    }

    @GetMapping("/roles")
    public ApiResponse<Map<String, Object>> getUserRoles(@AuthenticationPrincipal Jwt principal) {
        return ApiResponse.success(Map.of(
            "roles", auth0Service.getRolesFromToken(principal)
        ));
    }

    @GetMapping("/validate")
    public ApiResponse<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return ApiResponse.success(Map.of(
            "valid", !auth0Service.isTokenExpired(token),
            "email", auth0Service.getEmailFromToken(token)
        ));
    }
} 