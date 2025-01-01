package com.reelnet.infrastructure.monitoring.config;

import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpointDiscoverer;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.Properties;

@Configuration
public class MonitoringConfig {

    @Bean
    TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    WebMvcEndpointHandlerMapping webEndpointHandlerMapping(
            WebEndpointDiscoverer endpointDiscoverer,
            EndpointMediaTypes endpointMediaTypes,
            Environment environment) {
        
        String basePath = "/actuator";
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        
        return new WebMvcEndpointHandlerMapping(
                endpointMapping,
                endpointDiscoverer.getEndpoints(),
                endpointMediaTypes,
                null,
                new EndpointLinksResolver(endpointDiscoverer.getEndpoints()),
                true);
    }

    @Bean
    HttpExchangeRepository httpTraceRepository() {
        return new InMemoryHttpExchangeRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    BuildProperties buildProperties() {
        Properties props = new Properties();
        props.setProperty("version", "dev");
        props.setProperty("artifact", "ReelNet");
        props.setProperty("name", "ReelNet");
        props.setProperty("group", "com.reelnet");
        props.setProperty("time", String.valueOf(System.currentTimeMillis()));
        return new BuildProperties(props);
    }
} 