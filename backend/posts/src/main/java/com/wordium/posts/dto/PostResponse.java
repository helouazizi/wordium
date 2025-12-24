// src/main/java/com/wordium/posts/dto/PostResponse.java
package com.wordium.posts.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PostResponse(
        Long id,
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


