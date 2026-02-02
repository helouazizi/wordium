package com.wordium.posts.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisCacheConfig {

    // @Bean
    // public RedisCacheManager cacheManager(RedisConnectionFactory
    // connectionFactory) {

    // ObjectMapper objectMapper = new ObjectMapper();
    // objectMapper.registerModule(new JavaTimeModule());
    // objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // Jackson2JsonRedisSerializer<UserProfile> serializer = new
    // Jackson2JsonRedisSerializer<>(UserProfile.class);
    // serializer.setObjectMapper(objectMapper);

    // RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
    // .entryTtl(Duration.ofMinutes(10))
    // .disableCachingNullValues()
    // .serializeValuesWith(
    // RedisSerializationContext.SerializationPair.fromSerializer(serializer));

    // return RedisCacheManager.builder(connectionFactory)
    // .cacheDefaults(config)
    // .build();
    // }

}
