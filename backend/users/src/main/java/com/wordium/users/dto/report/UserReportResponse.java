package com.wordium.users.dto.report;

import java.time.LocalDateTime;

import com.wordium.users.dto.users.UserProfile;

public record UserReportResponse(
        Long id,
        Long reportedUserId,
        UserProfile reporter,
        String reason,
        String status,
        String type,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt) {
}
