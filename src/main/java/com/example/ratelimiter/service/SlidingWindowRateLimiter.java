package com.example.ratelimiter.service;

import com.example.ratelimiter.dto.RateLimitDecision;
import com.example.ratelimiter.redis.SlidingWindowLuaExecutor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class SlidingWindowRateLimiter {
    private final SlidingWindowLuaExecutor executor;

    private static final int limit = 5;
    private static final long windowSize = 60;

    public SlidingWindowRateLimiter(SlidingWindowLuaExecutor executor) {
        this.executor = executor;
    }

    public RateLimitDecision checkLimit(String key){
        long now = Instant.now().getEpochSecond();
        List result = executor.execute(key, now, windowSize, limit);

        Long allowedFlag = (Long) result.get(0);
        Long remaining = (Long) result.get(1);

        boolean allowed = allowedFlag == 1;
        long resetTime = now + windowSize;

        return new RateLimitDecision(allowed, limit, remaining, resetTime);
    }
}
