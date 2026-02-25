package com.example.ratelimiter.filter;

import com.example.ratelimiter.service.FixedWindowRateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimiterFilter extends OncePerRequestFilter {
    private final FixedWindowRateLimiter rateLimiter;

    public RateLimiterFilter(FixedWindowRateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        String key = "rate_limit:"+clientIp;

        boolean allowed = rateLimiter.isAllowed(key);

        if (!allowed) {
            response.setStatus(429);
            response.getWriter().write("Too many requests. Please try again later.");
            return;
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
