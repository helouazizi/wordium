package com.wordium.posts.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.wordium.posts.clients.UsersServiceClient;
import com.wordium.posts.dto.BatchUsersRequest;
import com.wordium.posts.dto.UserProfile;

@Service
public class ResolveUsersProfile {

    private static final String USER_CACHE = "user-profile";

    private final UsersServiceClient usersServiceClient;
    private final Cache cache;

    public ResolveUsersProfile(
        UsersServiceClient usersServiceClient,
        CacheManager cacheManager
    ) {
        this.usersServiceClient = usersServiceClient;
        this.cache = cacheManager.getCache(USER_CACHE);
    }

    public Map<Long, UserProfile> getUserProfiles(Set<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }

        Map<Long, UserProfile> result = new HashMap<>();
        Set<Long> missingIds = new HashSet<>();

        // Cache lookup
        for (Long id : userIds) {
            UserProfile cached = cache.get(id, UserProfile.class);
            if (cached != null) {
                result.put(id, cached);
            } else {
                missingIds.add(id);
            }
        }

        //Batch fetch missing
        if (!missingIds.isEmpty()) {
            BatchUsersRequest request =
                new BatchUsersRequest(new ArrayList<>(missingIds));

            List<UserProfile> fetched =
                usersServiceClient.getUsersByIds(request);

            for (UserProfile profile : fetched) {
                cache.put(profile.id(), profile);
                result.put(profile.id(), profile);
            }
        }

        return result;
    }

    public UserProfile getUserProfile(Long userId) {
        if (userId == null) {
            return null;
        }
        return getUserProfiles(Set.of(userId)).get(userId);
    }
}
