package com.spring.batch.config;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig {
    // Can be used to pass the some static data with different layers
    @Bean
    public ExecutionContext executionContext() {
        return new ExecutionContext();
    }
}
