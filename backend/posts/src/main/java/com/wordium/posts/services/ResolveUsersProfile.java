package com.wordium.posts.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.wordium.posts.clients.UsersServiceClient;
import com.wordium.posts.dto.BatchUsersRequest;
import com.wordium.posts.dto.UserProfile;

@Service
public class ResolveUsersProfile {

    private final UsersServiceClient usersServiceClient;
    private final RedisUserProfileService redisUserProfileService;

    public ResolveUsersProfile(
            UsersServiceClient usersServiceClient,
            RedisUserProfileService redisUserProfileService) {
        this.usersServiceClient = usersServiceClient;
        this.redisUserProfileService = redisUserProfileService;
    }

    public Map<Long, UserProfile> getUserProfiles(Set<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }

        Map<Long, UserProfile> result = new HashMap<>();
        Set<Long> missingIds = new HashSet<>();

        for (Long id : userIds) {
            UserProfile cached = redisUserProfileService.getUserProfile(id);
            if (cached != null) {
                result.put(id, cached);
            } else {
                missingIds.add(id);
            }
        }

        if (!missingIds.isEmpty()) {
            BatchUsersRequest request = new BatchUsersRequest(new ArrayList<>(missingIds));
            List<UserProfile> fetched = usersServiceClient.getUsersByIds(request);

            for (UserProfile profile : fetched) {
                redisUserProfileService.cacheUserProfile(profile);
                result.put(profile.id(), profile);
            }
        }

        return result;
    }

    public UserProfile getUserProfile(Long userId) {
        if (userId == null)
            return null;
        return getUserProfiles(Set.of(userId)).get(userId);
    }

    public void invalidateUserProfile(Long userId) {
        if (userId != null) {
            redisUserProfileService.deleteUserProfile(userId);
        }
    }
}
