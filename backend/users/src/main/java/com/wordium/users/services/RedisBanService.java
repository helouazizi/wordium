package com.wordium.users.services;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisBanService {

    private static final String BANNED_USER_KEY_PREFIX = "banned_user:";
    private static final Duration BAN_DURATION = Duration.ofHours(24); // 24 hours TTL

    private final RedisTemplate<String, String> redisTemplate;

    public RedisBanService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

 
    public void banUser(Long userId) {
        String key = BANNED_USER_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(key, "true", BAN_DURATION);
    }

   
    public void unbanUser(Long userId) {
        String key = BANNED_USER_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }

    public boolean isBanned(Long userId) {
        String key = BANNED_USER_KEY_PREFIX + userId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
