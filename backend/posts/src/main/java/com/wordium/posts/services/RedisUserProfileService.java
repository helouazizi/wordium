package com.wordium.posts.services;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.wordium.posts.dto.UserProfile;

@Service
public class RedisUserProfileService {

    private static final String USER_PROFILE_KEY_PREFIX = "user_profile:";
    private static final Duration TTL = Duration.ofMinutes(10); // default TTL for profiles

    private final RedisTemplate<String, UserProfile> redisTemplate;

    public RedisUserProfileService(RedisTemplate<String, UserProfile> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheUserProfile(UserProfile profile) {
        String key = USER_PROFILE_KEY_PREFIX + profile.id();
        redisTemplate.opsForValue().set(key, profile, TTL);
    }

    public UserProfile getUserProfile(Long userId) {
        String key = USER_PROFILE_KEY_PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteUserProfile(Long userId) {
        String key = USER_PROFILE_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }

    public boolean exists(Long userId) {
        String key = USER_PROFILE_KEY_PREFIX + userId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
