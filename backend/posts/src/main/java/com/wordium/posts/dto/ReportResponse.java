package com.wordium.posts.dto;

import java.time.LocalDateTime;

public record ReportResponse(
        Long id,
        UserProfile reporter,
        Long reportedPostId,
        UserProfile reportedUser,
        String reason,
        UserProfile resolvedById,
        LocalDateTime resolvedAt,
        LocalDateTime createdAt
) {}
