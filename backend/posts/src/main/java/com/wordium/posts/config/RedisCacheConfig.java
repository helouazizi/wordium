package com.wordium.posts.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wordium.posts.dto.UserProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisTemplate<String, UserProfile> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, UserProfile> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key serializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value serializer with JavaTimeModule
        Jackson2JsonRedisSerializer<UserProfile> serializer = new Jackson2JsonRedisSerializer<>(UserProfile.class);

        // Configure ObjectMapper manually to support Java 8 date/time
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Supports LocalDateTime
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO-8601 instead of timestamps

        serializer.setObjectMapper(mapper); // Deprecated warning is okay if no constructor exists

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
