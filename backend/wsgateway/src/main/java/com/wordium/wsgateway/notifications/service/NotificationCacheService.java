package com.wordium.wsgateway.notifications.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.wordium.wsgateway.notifications.model.Notification;

@Service
public class NotificationCacheService {

    private static final int MAX_LATEST = 50;

    private final StringRedisTemplate redis;

    public NotificationCacheService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void cache(Notification n) {

        String userId = n.getReceiverUserId().toString();

        // unread count
        redis.opsForValue()
                .increment(unreadKey(userId));

        // latest notifications
        redis.opsForList().leftPush(
                latestKey(userId),
                toJson(n)
        );

        redis.opsForList().trim(
                latestKey(userId),
                0, MAX_LATEST - 1
        );
    }

    private String unreadKey(String userId) {
        return "notifications:unread:" + userId;
    }

    private String latestKey(String userId) {
        return "notifications:latest:" + userId;
    }

    private String toJson(Notification n) {
        // use Jackson ObjectMapper bean
        // keep it simple for now
        return "{\"id\":" + n.getId() + ",\"type\":\"" + n.getType() + "\"}";
    }

    public void decrementUnread(Long userId) {
        String key = unreadKey(userId.toString());
        // decrement, but never go below 0
        redis.opsForValue().decrement(key);

        // Optional safety check:
        Long count = Long.parseLong(redis.opsForValue().get(key));
        if (count < 0) {
            redis.opsForValue().set(key, "0");
        }
    }

    // public Optional<List<Notification>> getLatest(Long userId) {
    //     List<String> data = redis.opsForList().range(latestKey(userId.toString()), 0, MAX_LATEST - 1);
    //     if (data == null || data.isEmpty()) {
    //         return Optional.empty();
    //     }

    //     List<Notification> notifications = data.stream()
    //             .map(this::fromJson) // parse JSON into Notification objects
    //             .toList();
    //     return Optional.of(notifications);
    // }


    // public  fromJson(Notification n){
    //     // check if data extracrted is valid into Notifications 

    // }

}
