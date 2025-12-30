package com.wordium.posts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        Long commentId,
        @NotBlank(message = "Content is required")
        @Size(max = 500, message = "content cannot exceed 500 characters")
        String content
        ) {

}
