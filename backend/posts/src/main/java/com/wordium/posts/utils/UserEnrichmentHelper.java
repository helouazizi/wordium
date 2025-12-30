package com.wordium.posts.utils;


import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.wordium.posts.dto.UserProfile;
import com.wordium.posts.services.ResolveUsersProfile;

@Component
public class UserEnrichmentHelper {

    private final ResolveUsersProfile resolveUsersProfile;

    public UserEnrichmentHelper(ResolveUsersProfile resolveUsersProfile) {
        this.resolveUsersProfile = resolveUsersProfile;
    }


    // public <E, R> List<R> enrichList(
    //         List<E> entities,
    //         Function<E, Long> userIdExtractor,
    //         java.util.function.BiFunction<E, UserProfile, R> mapper) {

    //     if (entities.isEmpty()) {
    //         return List.of();
    //     }

    //     Set<Long> userIds = entities.stream()
    //             .map(userIdExtractor)
    //             .filter(Objects::nonNull)
    //             .collect(Collectors.toSet());

    //     Map<Long, UserProfile> userMap = resolveUsersProfile.getUserProfiles(userIds);

    //     return entities.stream()
    //             .map(entity -> mapper.apply(entity, userMap.get(userIdExtractor.apply(entity))))
    //             .toList();
    // }


    public <E, R> R enrichSingle(
            E entity,
            Function<E, Long> userIdExtractor,
            java.util.function.BiFunction<E, UserProfile, R> mapper) {

        if (entity == null) {
            return null;
        }

        Long userId = userIdExtractor.apply(entity);
        UserProfile userProfile = userId != null ? resolveUsersProfile.getUserProfile(userId) : null;

        return mapper.apply(entity, userProfile);
    }

    public <E, R> org.springframework.data.domain.Page<R> enrichPage(
            org.springframework.data.domain.Page<E> page,
            Function<E, Long> userIdExtractor,
            java.util.function.BiFunction<E, UserProfile, R> mapper) {

        if (page.isEmpty()) {
            return page.map(e -> mapper.apply(e, null));
        }

        Set<Long> userIds = page.getContent().stream()
                .map(userIdExtractor)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, UserProfile> userMap = resolveUsersProfile.getUserProfiles(userIds);

        return page.map(entity -> mapper.apply(entity, userMap.get(userIdExtractor.apply(entity))));
    }
}