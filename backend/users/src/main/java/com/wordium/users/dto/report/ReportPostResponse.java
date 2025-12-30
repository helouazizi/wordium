package com.wordium.users.dto.report;

import java.time.LocalDateTime;

import com.wordium.users.dto.users.UserProfile;

public record ReportPostResponse(
        Long id,
        UserProfile reporter,
        Long reportedPostId,
        String reason,
        boolean resolved,
        LocalDateTime resolvedAt,
        LocalDateTime createdAt

) {}