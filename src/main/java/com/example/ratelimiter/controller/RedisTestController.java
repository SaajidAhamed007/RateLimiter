package com.example.ratelimiter.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.redis.core.StringRedisTemplate;

@RestController
public class RedisTestController {
    private final StringRedisTemplate redisTemplate;

    public RedisTestController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/redis-test")
    public String testRedis() {
        redisTemplate.opsForValue().set("test", "Hello, Redis!");
        return redisTemplate.opsForValue().get("test");  
    }

    @GetMapping("/api/data")
    public String getData() {
        return "Success";
    }
}
