package com.example.ratelimiter.policy;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InMemoryPolicyResolver implements PolicyResolver {

    private static final Map<String, RateLimitPolicy> policies = Map.of(
            "/login", new RateLimitPolicy(5, 60),
            "/api/data", new RateLimitPolicy(100, 60),
            "/api/admin", new RateLimitPolicy(10, 60)
    );

    private static final RateLimitPolicy DEFAULT_POLICY =
            new RateLimitPolicy(50, 60);

    @Override
    public RateLimitPolicy resolve(HttpServletRequest request) {

        String path = request.getRequestURI();

        return policies.getOrDefault(path, DEFAULT_POLICY);
    }
}