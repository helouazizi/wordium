package com.wordium.posts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.wordium.posts.dto.UserProfile;

@Configuration
public class RedisCacheConfig {
    @Bean
    public RedisTemplate<String, UserProfile> redisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, UserProfile> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

}
