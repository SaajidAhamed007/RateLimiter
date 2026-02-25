package com.example.ratelimiter.dto;

public record RateLimitDecision(boolean allowed, long limit, long remaining, long resetEpochSeconds) {
}

