package com.reelnet.infrastructure.security.filter;

import com.reelnet.infrastructure.security.service.Auth0AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class Auth0SecurityFilter extends OncePerRequestFilter {

    private final Auth0AuthenticationService auth0Service;
    private final JwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractToken(request);
            if (token != null && !auth0Service.isTokenExpired(token)) {
                Jwt jwt = jwtDecoder.decode(token);
                var authorities = auth0Service.extractAuthorities(jwt);

                var authentication = new UsernamePasswordAuthenticationToken(
                        auth0Service.getEmailFromToken(token),
                        null,
                        authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Add user info to request attributes for controllers
                request.setAttribute("user_email", auth0Service.getEmailFromToken(token));
                request.setAttribute("user_roles", auth0Service.getRolesFromToken(jwt));
            }
        } catch (Exception e) {
            log.error("Could not authenticate user", e);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/public") ||
                path.startsWith("/api/auth") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/actuator/health");
    }
}