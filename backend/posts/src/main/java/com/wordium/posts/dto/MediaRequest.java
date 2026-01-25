package com.wordium.posts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MediaRequest(
        @NotBlank String publicId,
        @NotNull MediaType type) {
}
