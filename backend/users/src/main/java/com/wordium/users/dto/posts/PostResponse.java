package com.wordium.users.dto.posts;



import java.time.LocalDateTime;
import java.util.List;

import com.wordium.users.dto.users.UserProfile;

public record PostResponse(
        Long id,
        String title,
        String content,
        UserProfile actor,
        List<PostImageResponse> images,
        long likesCount,
        long commentsCount,
        long reportsCount,
        boolean isReported,
        boolean isFlagged,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        ) {

}