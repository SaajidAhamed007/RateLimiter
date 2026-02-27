package com.example.ratelimiter.policy;

public record RateLimitPolicy(
        long limit,
        long windowSize
) {}