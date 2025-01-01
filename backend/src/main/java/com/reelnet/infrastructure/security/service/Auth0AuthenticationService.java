package com.reelnet.infrastructure.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class Auth0AuthenticationService {

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.domain}")
    private String domain;

    @Value("${auth0.client-id}")
    private String clientId;

    @Value("${auth0.client-secret}")
    private String clientSecret;

    public Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        var scope = jwt.getClaimAsString("scope");
        var permissions = scope != null ? Arrays.asList(scope.split(" ")) : Collections.<String>emptyList();
        
        var authorities = new HashSet<GrantedAuthority>();
        
        // Add permissions as authorities
        permissions.forEach(permission ->
            authorities.add(new SimpleGrantedAuthority("SCOPE_" + permission))
        );
        
        // Add roles from Auth0
        var roles = getRolesFromToken(jwt);
        roles.forEach(role ->
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role))
        );
        
        return authorities;
    }

    public Set<String> getRolesFromToken(Jwt jwt) {
        var roles = jwt.getClaimAsStringList("https://" + domain + "/roles");
        return roles != null ? new HashSet<>(roles) : Collections.emptySet();
    }

    public String getEmailFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("email").asString();
        } catch (Exception e) {
            log.error("Error extracting email from token", e);
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt().before(new Date());
        } catch (Exception e) {
            log.error("Error checking token expiration", e);
            return true;
        }
    }

    public Map<String, Object> getUserInfo(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("email", jwt.getClaim("email").asString());
            userInfo.put("name", jwt.getClaim("name").asString());
            userInfo.put("picture", jwt.getClaim("picture").asString());
            userInfo.put("roles", getRolesFromToken(jwt));
            return userInfo;
        } catch (Exception e) {
            log.error("Error extracting user info from token", e);
            return Collections.emptyMap();
        }
    }

    private Set<String> getRolesFromToken(DecodedJWT jwt) {
        try {
            String[] roles = jwt.getClaim("https://" + domain + "/roles").asArray(String.class);
            return roles != null ? new HashSet<>(Arrays.asList(roles)) : Collections.emptySet();
        } catch (Exception e) {
            log.error("Error extracting roles from token", e);
            return Collections.emptySet();
        }
    }
} 