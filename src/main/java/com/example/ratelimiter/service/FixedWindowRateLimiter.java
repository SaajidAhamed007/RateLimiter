package com.example.ratelimiter.service;

import org.springframework.stereotype.Service;
import com.example.ratelimiter.dto.RateLimitDecision;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Instant;

@Service
public class FixedWindowRateLimiter {
    private final StringRedisTemplate redisTemplate;
    private final int limit = 5; // Max requests per window
    private final long windowSize = 60; // Window size in seconds

    public FixedWindowRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RateLimitDecision checkLimit(String key){
        Long count = redisTemplate.opsForValue().increment(key);

        if(count==null){
            count=0L;
        }

        if(count==1){
            redisTemplate.expire(key,windowSize,java.util.concurrent.TimeUnit.SECONDS);
        }

        Long ttl = redisTemplate.getExpire(key);
        long resetTime = Instant.now().getEpochSecond() + (ttl!=null ? ttl : 0);
        long remaining = Math.max(0, limit - count);

        return new RateLimitDecision(count <= limit, limit, remaining, resetTime);
    }
}
