# Security Documentation

## Overview

This document outlines the security measures, best practices, and configurations implemented in ReelNet to ensure data protection and system security.

## Authentication & Authorization

### JWT Implementation

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and().csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager()))
            .addFilter(new JWTAuthorizationFilter(authenticationManager()))
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
```

### Token Management

1. **Token Generation**
   ```java
   public String generateToken(UserDetails userDetails) {
       Map<String, Object> claims = new HashMap<>();
       return Jwts.builder()
           .setClaims(claims)
           .setSubject(userDetails.getUsername())
           .setIssuedAt(new Date(System.currentTimeMillis()))
           .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
           .signWith(SignatureAlgorithm.HS512, secret)
           .compact();
   }
   ```

2. **Token Validation**
   ```java
   public Boolean validateToken(String token, UserDetails userDetails) {
       final String username = getUsernameFromToken(token);
       return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
   }
   ```

## Password Security

### Password Policy

```java
@Component
public class PasswordValidator {
    private static final String PASSWORD_PATTERN = 
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    public boolean isValid(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }
}
```

### Password Hashing

```java
@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser(UserDTO userDTO) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // Additional user setup
    }
}
```

## API Security

### CORS Configuration

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(environment.getProperty("allowed.origins"))
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
```

### Rate Limiting

```java
@Component
public class RateLimitingFilter extends OncePerRequestFilter {
    
    private RateLimiter rateLimiter = RateLimiter.create(100.0); // 100 requests per second

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        if (!rateLimiter.tryAcquire()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return;
        }
        filterChain.doFilter(request, response);
    }
}
```

## Data Protection

### Encryption at Rest

```java
@Configuration
public class EncryptionConfig {
    
    @Bean
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(environment.getProperty("encryption.key"));
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
```

### Data Masking

```java
@JsonComponent
public class DataMaskingSerializer extends JsonSerializer<String> {
    
    @Override
    public void serialize(String value, JsonGenerator gen, 
                         SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        gen.writeString(maskSensitiveData(value));
    }

    private String maskSensitiveData(String data) {
        if (data.length() <= 4) {
            return "****";
        }
        return "****" + data.substring(data.length() - 4);
    }
}
```

## Network Security

### SSL/TLS Configuration

```yaml
server:
  ssl:
    key-store: classpath:keystore.p12
    key-store-password: ${SSL_KEY_STORE_PASSWORD}
    key-store-type: PKCS12
    key-alias: reelnet
    enabled: true
```

### Firewall Rules

```yaml
# AWS Security Group Configuration
resource "aws_security_group" "application" {
  name        = "application"
  description = "Security group for application servers"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
```

## File Upload Security

### File Validation

```java
@Service
public class FileValidationService {
    
    private static final List<String> ALLOWED_EXTENSIONS = 
        Arrays.asList("jpg", "jpeg", "png", "mp4");
    private static final long MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB

    public void validateFile(MultipartFile file) throws ValidationException {
        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ValidationException("File size exceeds maximum limit");
        }

        // Check file extension
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new ValidationException("File type not allowed");
        }

        // Scan for malware
        scanForMalware(file);
    }
}
```

### Secure File Storage

```java
@Service
public class S3StorageService {
    
    @Autowired
    private AmazonS3 s3Client;

    public String storeFile(MultipartFile file, String key) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // Server-side encryption
        metadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);

        PutObjectRequest request = new PutObjectRequest(bucketName, key, 
            file.getInputStream(), metadata)
            .withCannedAcl(CannedAccessControlList.Private);

        s3Client.putObject(request);
        return key;
    }
}
```

## Audit Logging

### Security Events

```java
@Aspect
@Component
public class SecurityAuditAspect {
    
    @Autowired
    private AuditLogService auditLogService;

    @AfterReturning("execution(* com.reelnet.security.*.*(..))")
    public void logSecurityEvent(JoinPoint joinPoint) {
        AuditLog log = new AuditLog();
        log.setEventType("SECURITY");
        log.setMethod(joinPoint.getSignature().getName());
        log.setTimestamp(LocalDateTime.now());
        log.setUser(SecurityContextHolder.getContext().getAuthentication().getName());
        
        auditLogService.save(log);
    }
}
```

### Access Logging

```java
@Component
public class AccessLogFilter extends OncePerRequestFilter {
    
    @Autowired
    private AccessLogService accessLogService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        AccessLog log = new AccessLog();
        log.setPath(request.getRequestURI());
        log.setMethod(request.getMethod());
        log.setIpAddress(request.getRemoteAddr());
        log.setTimestamp(LocalDateTime.now());
        
        accessLogService.save(log);
        
        filterChain.doFilter(request, response);
    }
}
```

## Security Headers

```java
@Configuration
public class SecurityHeadersConfig {
    
    @Bean
    public FilterRegistrationBean<HeaderWriterFilter> securityHeadersFilter() {
        HeaderWriterFilter headersFilter = new HeaderWriterFilter(Arrays.asList(
            new XContentTypeOptionsHeaderWriter(),
            new XXssProtectionHeaderWriter(),
            new XFrameOptionsHeaderWriter(),
            new HstsHeaderWriter(),
            new CacheControlHeadersWriter(),
            new ContentSecurityPolicyHeaderWriter("default-src 'self'")
        ));

        FilterRegistrationBean<HeaderWriterFilter> registration = 
            new FilterRegistrationBean<>(headersFilter);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
```

## Security Monitoring

### Alert Configuration

```java
@Service
public class SecurityMonitoringService {
    
    @Autowired
    private AlertService alertService;

    @Scheduled(fixedRate = 300000) // Every 5 minutes
    public void monitorFailedLogins() {
        int failedLogins = getFailedLoginCount(Duration.ofMinutes(5));
        if (failedLogins > 100) {
            alertService.sendAlert(new SecurityAlert(
                "High number of failed logins detected",
                SecurityLevel.HIGH
            ));
        }
    }
}
```

### Incident Response

```java
@Service
public class IncidentResponseService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private NotificationService notificationService;

    public void handleSecurityIncident(SecurityIncident incident) {
        // Lock affected accounts
        incident.getAffectedUsers().forEach(user -> 
            userService.lockAccount(user.getId()));

        // Notify security team
        notificationService.notifySecurityTeam(incident);

        // Log incident
        logIncident(incident);
    }
}
```

## Security Best Practices

1. **Input Validation**
   ```java
   public class InputValidator {
       public static String sanitizeInput(String input) {
           return Jsoup.clean(input, Whitelist.none());
       }
   }
   ```

2. **Session Management**
   ```java
   @Configuration
   public class SessionConfig extends WebSecurityConfigurerAdapter {
       @Override
       protected void configure(HttpSecurity http) throws Exception {
           http.sessionManagement()
               .maximumSessions(1)
               .maxSessionsPreventsLogin(true)
               .expiredUrl("/login?expired");
       }
   }
   ```

3. **Error Handling**
   ```java
   @ControllerAdvice
   public class SecurityExceptionHandler {
       @ExceptionHandler(SecurityException.class)
       public ResponseEntity<ErrorResponse> handleSecurityException(
           SecurityException ex) {
           // Log security exception
           // Return sanitized error response
           return ResponseEntity.status(HttpStatus.FORBIDDEN)
               .body(new ErrorResponse("Access Denied"));
       }
   }
   ```

For more information about security features and implementation details, refer to:
- [Authentication Documentation](../api/auth.md)
- [Infrastructure Security](../architecture/infrastructure.md)
- [Compliance Guidelines](./compliance.md) 