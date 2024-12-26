# Backend Architecture

## Overview

The ReelNet backend is built using Spring Boot following Domain-Driven Design (DDD) principles and Clean Architecture patterns.

## Architecture Layers

### 1. Application Layer
- REST Controllers
- GraphQL Resolvers
- WebSocket Controllers
- Request/Response DTOs
- Input Validation
- Authentication/Authorization

### 2. Domain Layer
- Business Logic
- Domain Models
- Domain Services
- Domain Events
- Business Rules
- Value Objects

### 3. Infrastructure Layer
- Data Persistence
- External Services Integration
- Message Queues
- Caching
- File Storage

## Key Components

### Authentication Service
```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // Login
    // Registration
    // Password Reset
    // Token Refresh
}
```

### User Management
```java
// User Entity Structure
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles;
    // Additional fields
}
```

### Project Management
```java
public class Project {
    private Long id;
    private String name;
    private String description;
    private User owner;
    private Set<User> collaborators;
    private ProjectStatus status;
    // Additional fields
}
```

## Database Schema

### Core Tables
- users
- projects
- collaborations
- videos
- comments
- notifications

## API Endpoints

### Authentication
- POST /api/auth/login
- POST /api/auth/register
- POST /api/auth/refresh
- POST /api/auth/logout

### Projects
- GET /api/projects
- POST /api/projects
- GET /api/projects/{id}
- PUT /api/projects/{id}
- DELETE /api/projects/{id}

### Collaboration
- POST /api/projects/{id}/collaborators
- GET /api/projects/{id}/collaborators
- DELETE /api/projects/{id}/collaborators/{userId}

## Security Implementation

### JWT Configuration
```java
@Configuration
public class SecurityConfig {
    // JWT Token configuration
    // Security filters
    // Authentication providers
}
```

## Real-time Features

### WebSocket Configuration
```java
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    // WebSocket endpoints
    // Message handlers
    // Session management
}
```

## Error Handling

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    // Handle various exceptions
    // Return appropriate error responses
}
```

## Performance Optimizations

1. **Caching Strategy**
   - Redis for session data
   - Local caching for static data
   - Query result caching

2. **Database Optimization**
   - Indexing strategy
   - Query optimization
   - Connection pooling

3. **Resource Management**
   - Connection pooling
   - Thread pool configuration
   - Memory management

## Monitoring and Logging

1. **Logging Configuration**
   - Log levels
   - Log rotation
   - Error tracking

2. **Metrics**
   - Performance metrics
   - Business metrics
   - System health

## Deployment Configuration

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/reelnet
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: none
  redis:
    host: ${REDIS_HOST}
    port: ${REDIS_PORT}
```

For more detailed information about specific components, refer to:
- [API Documentation](../api/)
- [Security Documentation](../technical/security.md)
- [Performance Optimization](../technical/performance.md) 