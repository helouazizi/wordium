
package com.wordium.posts.dto;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String content,
        UserProfile actor,
        long likesCount,
        long commentsCount,
        long reportsCount,
        boolean isReported,
        boolean isFlagged,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        ) {

}


