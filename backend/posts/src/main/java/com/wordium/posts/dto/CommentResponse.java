package com.wordium.posts.dto;


import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long postId,
        String content,
        LocalDateTime createdAt,
        UserProfile actor
) {}
