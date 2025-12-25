package com.wordium.posts.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.wordium.posts.clients.UsersServiceClient;
import com.wordium.posts.dto.BatchUsersRequest;
import com.wordium.posts.dto.UserProfile;

@Service
public class ResolveUsersProfile {

    private final UsersServiceClient usersServiceClient;

    public ResolveUsersProfile(UsersServiceClient usersServiceClient) {
        this.usersServiceClient = usersServiceClient;
    }

    @Cacheable(value = "usersProfiles", key = "#usersIds.hashCode()", condition = "#usersIds?.size() > 0")
    public Map<Long, UserProfile> getUserProfiles(Set<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Map.of();
        }

        BatchUsersRequest request = new BatchUsersRequest(new ArrayList<>(userIds));
        List<UserProfile> profiles = usersServiceClient.getUsersByIds(request);

        return profiles.stream()
                .collect(Collectors.toMap(UserProfile::id, profile -> profile));
    }

    public UserProfile getUserProfile(Long userId) {
        if (userId == null) {
            return null;
        }
        return getUserProfiles(Set.of(userId)).get(userId);
    }
}
