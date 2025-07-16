package com.example.marketingagencymanagement.config;


import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public RateLimiter basicRateLimiter() {
        return RateLimiter.create(10.0 / 60.0);
    }

    @Bean
    public RateLimiter standardRateLimiter() {
        return RateLimiter.create(100.0 / 60.0);
    }

    @Bean
    public RateLimiter goldenRateLimiter() {
        return RateLimiter.create(10000.0 / 60.0);
    }
}
