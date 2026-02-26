package com.example.ratelimiter.redis;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SlidingWindowLuaExecutor {
    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<List> script;

    public SlidingWindowLuaExecutor(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;

        script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("scripts/slidingwindow.lua"));
        script.setResultType(List.class);
    }

    public List execute(String key, long now, long windowSize, long limit) {
        return redisTemplate.execute(script, List.of(key), String.valueOf(now), String.valueOf(windowSize), String.valueOf(limit));
    }
}
