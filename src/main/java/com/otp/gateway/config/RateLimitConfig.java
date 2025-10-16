package com.otp.gateway.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Value("${application.ratelimit.requests-per-second}")
    private int requestsPerSecond;

    @Bean
    public Bucket bucket() {
        Bandwidth limit = Bandwidth.classic(requestsPerSecond, Refill.greedy(requestsPerSecond, Duration.ofSeconds(1)));
        return Bucket.builder().addLimit(limit).build();
    }
}