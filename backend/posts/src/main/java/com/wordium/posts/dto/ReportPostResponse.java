package com.wordium.posts.dto;

import java.time.LocalDateTime;

public record ReportPostResponse(
        Long id,
        UserProfile reporter,
        Long reportedPostId,
        String reason,
        boolean resolved,
        LocalDateTime resolvedAt,
        LocalDateTime createdAt

) {}
