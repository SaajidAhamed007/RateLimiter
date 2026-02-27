package com.example.ratelimiter.filter;

import com.example.ratelimiter.dto.RateLimitDecision;
import com.example.ratelimiter.policy.PolicyResolver;
import com.example.ratelimiter.policy.RateLimitPolicy;
import com.example.ratelimiter.service.FixedWindowRateLimiter;
import com.example.ratelimiter.service.SlidingWindowRateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimiterFilter extends OncePerRequestFilter {
    private final SlidingWindowRateLimiter rateLimiter;
    private final PolicyResolver policyResolver;

    public RateLimiterFilter(SlidingWindowRateLimiter rateLimiter, PolicyResolver policyResolver) {
        this.rateLimiter = rateLimiter;
        this.policyResolver = policyResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        String endpoint = request.getRequestURI();
        String key = "rl:"+clientIp+":"+endpoint;

        RateLimitPolicy policy = policyResolver.resolve(request);

        RateLimitDecision decision = rateLimiter.checkLimit(key, policy.windowSize(), policy.limit());

        response.setHeader("X-RateLimit-Limit", String.valueOf(decision.limit()));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(decision.remaining()));
        response.setHeader("X-RateLimit-Reset", String.valueOf(decision.resetEpochSeconds()));

        if(!decision.allowed()){
            response.setStatus(429);
            response.getWriter().write("Too many requests. Please try again later.");
            return;
        }

        filterChain.doFilter(request, response);
    }

}
