package com.example.ratelimiter.policy;

import jakarta.servlet.http.HttpServletRequest;;

public interface PolicyResolver {

    RateLimitPolicy resolve(HttpServletRequest request);

}