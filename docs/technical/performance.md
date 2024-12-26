# Performance Optimization Guide

## Overview

This document outlines performance optimization strategies, monitoring solutions, and best practices implemented in ReelNet.

## Backend Performance

### Database Optimization

1. **Indexing Strategy**
   ```sql
   -- User table indexes
   CREATE INDEX idx_users_username ON users(username);
   CREATE INDEX idx_users_email ON users(email);
   
   -- Project table indexes
   CREATE INDEX idx_projects_owner_id ON projects(owner_id);
   CREATE INDEX idx_projects_created_at ON projects(created_at);
   
   -- Composite indexes for common queries
   CREATE INDEX idx_projects_owner_status ON projects(owner_id, status);
   ```

2. **Query Optimization**
   ```java
   @Repository
   public interface ProjectRepository extends JpaRepository<Project, Long> {
       
       // Use specific columns instead of SELECT *
       @Query("SELECT new com.reelnet.dto.ProjectSummary(p.id, p.name, p.status) " +
              "FROM Project p WHERE p.owner.id = :ownerId")
       List<ProjectSummary> findProjectSummariesByOwner(@Param("ownerId") Long ownerId);
       
       // Use pagination for large result sets
       @Query("SELECT p FROM Project p WHERE p.status = :status")
       Page<Project> findByStatus(@Param("status") String status, Pageable pageable);
   }
   ```

### Caching Strategy

1. **Redis Configuration**
   ```java
   @Configuration
   @EnableCaching
   public class CacheConfig {
       
       @Bean
       public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
           RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
               .entryTtl(Duration.ofMinutes(10))
               .serializeKeysWith(RedisSerializationContext.SerializationPair
                   .fromSerializer(new StringRedisSerializer()))
               .serializeValuesWith(RedisSerializationContext.SerializationPair
                   .fromSerializer(new GenericJackson2JsonRedisSerializer()));
           
           return RedisCacheManager.builder(connectionFactory)
               .cacheDefaults(config)
               .build();
       }
   }
   ```

2. **Cache Implementation**
   ```java
   @Service
   public class ProjectService {
       
       @Cacheable(value = "projects", key = "#id")
       public Project getProject(Long id) {
           return projectRepository.findById(id)
               .orElseThrow(() -> new ProjectNotFoundException(id));
       }
       
       @CacheEvict(value = "projects", key = "#id")
       public void updateProject(Long id, ProjectDTO projectDTO) {
           Project project = getProject(id);
           // Update project
           projectRepository.save(project);
       }
   }
   ```

### Connection Pooling

```yaml
# application.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      idle-timeout: 300000
      connection-timeout: 20000
      max-lifetime: 1200000
```

## Frontend Performance

### Code Splitting

```typescript
// React lazy loading
const VideoEditor = React.lazy(() => import('./components/VideoEditor'));
const ProjectDashboard = React.lazy(() => import('./components/ProjectDashboard'));

function App() {
  return (
    <Suspense fallback={<Loading />}>
      <Route path="/editor/:id" component={VideoEditor} />
      <Route path="/dashboard" component={ProjectDashboard} />
    </Suspense>
  );
}
```

### Bundle Optimization

```javascript
// vite.config.js
export default defineConfig({
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          vendor: ['react', 'react-dom', 'react-router-dom'],
          editor: ['@ffmpeg/ffmpeg', '@ffmpeg/core'],
        },
      },
    },
    minify: 'terser',
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true,
      },
    },
  },
});
```

### Image Optimization

```typescript
// Image optimization component
const OptimizedImage: React.FC<ImageProps> = ({ src, alt, size }) => {
  const [loaded, setLoaded] = useState(false);
  
  return (
    <>
      <img
        src={`${src}?w=${size}&q=75`}
        alt={alt}
        loading="lazy"
        onLoad={() => setLoaded(true)}
        style={{ opacity: loaded ? 1 : 0 }}
      />
      {!loaded && <Placeholder />}
    </>
  );
};
```

## Video Processing Optimization

### FFmpeg Configuration

```java
@Service
public class VideoProcessingService {
    
    private static final Map<String, String> ENCODING_PRESETS = Map.of(
        "fast", "-preset veryfast -crf 23",
        "balanced", "-preset medium -crf 20",
        "quality", "-preset slower -crf 18"
    );
    
    public void processVideo(String inputPath, String outputPath, String preset) {
        String ffmpegCommand = String.format(
            "ffmpeg -i %s %s %s",
            inputPath,
            ENCODING_PRESETS.get(preset),
            outputPath
        );
        // Execute command
    }
}
```

### Parallel Processing

```java
@Service
public class VideoProcessingService {
    
    @Autowired
    private ExecutorService executorService;
    
    public CompletableFuture<Void> processVideoSegments(List<VideoSegment> segments) {
        return CompletableFuture.allOf(
            segments.stream()
                .map(segment -> CompletableFuture.runAsync(
                    () -> processSegment(segment),
                    executorService
                ))
                .toArray(CompletableFuture[]::new)
        );
    }
}
```

## API Performance

### Response Compression

```java
@Configuration
public class CompressionConfig {
    
    @Bean
    public GzipRequestFilter gzipRequestFilter() {
        return new GzipRequestFilter();
    }
    
    @Bean
    public FilterRegistrationBean<GzipResponseFilter> gzipResponseFilter() {
        FilterRegistrationBean<GzipResponseFilter> filterBean = 
            new FilterRegistrationBean<>(new GzipResponseFilter());
        filterBean.addUrlPatterns("/api/*");
        return filterBean;
    }
}
```

### Request/Response DTOs

```java
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    
    @JsonIgnore
    private LocalDateTime createdAt;
    
    @JsonProperty("owner")
    private UserSummaryDTO owner;
    
    // Only include necessary fields
}
```

## Load Testing

### JMeter Test Plan

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jmeterTestPlan version="1.2" properties="5.0">
  <hashTree>
    <TestPlan guiclass="TestPlanGui" testclass="TestPlan" testname="ReelNet Load Test">
      <elementProp name="TestPlan.user_defined_variables" elementType="Arguments">
        <collectionProp name="Arguments.arguments"/>
      </elementProp>
      <stringProp name="TestPlan.comments"></stringProp>
      <boolProp name="TestPlan.functional_mode">false</boolProp>
      <boolProp name="TestPlan.serialize_threadgroups">false</boolProp>
      <stringProp name="TestPlan.user_define_classpath"></stringProp>
    </TestPlan>
    <!-- Test configurations -->
  </hashTree>
</jmeterTestPlan>
```

### Performance Metrics

```java
@Configuration
public class MetricsConfig {
    
    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }
    
    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}

@Service
public class ProjectService {
    
    @Timed(value = "project.creation.time", description = "Time taken to create project")
    public Project createProject(ProjectDTO projectDTO) {
        // Project creation logic
    }
}
```

## Monitoring and Alerting

### Prometheus Configuration

```yaml
# prometheus.yml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'spring-actuator'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8080']
```

### Grafana Dashboard

```json
{
  "dashboard": {
    "id": null,
    "title": "ReelNet Performance Dashboard",
    "panels": [
      {
        "title": "API Response Time",
        "type": "graph",
        "datasource": "Prometheus",
        "targets": [
          {
            "expr": "http_server_requests_seconds_sum / http_server_requests_seconds_count",
            "legendFormat": "{{method}} {{uri}}"
          }
        ]
      }
    ]
  }
}
```

## Performance Best Practices

1. **Database**
   - Use appropriate indexes
   - Implement query caching
   - Regular maintenance and optimization
   - Connection pooling

2. **Application**
   - Implement caching strategies
   - Use asynchronous processing
   - Optimize resource usage
   - Regular performance testing

3. **Frontend**
   - Code splitting
   - Bundle optimization
   - Image optimization
   - Lazy loading

4. **API**
   - Response compression
   - Rate limiting
   - Pagination
   - Data filtering

## Performance Benchmarks

### API Response Times

```
Endpoint                 P95 (ms)    P99 (ms)    Max (ms)
GET /api/projects        150         300         500
POST /api/projects       200         400         600
GET /api/projects/{id}   100         200         400
```

### Video Processing

```
Operation               Average Time    Max Time
720p Transcoding       2 min          5 min
1080p Transcoding      5 min          10 min
4K Transcoding         15 min         30 min
```

For more information about performance optimization and monitoring, refer to:
- [Infrastructure Documentation](../architecture/infrastructure.md)
- [Monitoring Guide](./monitoring.md)
- [Deployment Guide](./deployment.md) 