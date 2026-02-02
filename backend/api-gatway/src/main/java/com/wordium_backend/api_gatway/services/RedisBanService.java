package com.wordium_backend.api_gatway.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisBanService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisBanService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String BANNED_USER_KEY_PREFIX = "banned_user:";

    public boolean isBanned(String userId) {
        String key = BANNED_USER_KEY_PREFIX + userId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
