package com.wordium.users.dto.report;

import jakarta.validation.constraints.NotBlank;

public record CreateUserReportRequest(
        @NotBlank String reason

) {
}
