package com.example.ratelimiter.service;

import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

@Service
public class FixedWindowRateLimiter {
    private final StringRedisTemplate redisTemplate;
    private final int limit = 5; // Max requests per window
    private final long windowSize = 60; // Window size in seconds

    public FixedWindowRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isAllowed(String key) {
        String redisKey = "rate_limiter:" + key;
        Long currentCount = redisTemplate.opsForValue().increment(redisKey);
        
        if (currentCount == 1) {
            redisTemplate.expire(redisKey, windowSize, java.util.concurrent.TimeUnit.SECONDS);
        }
        
        return currentCount <= limit;
    }
}
