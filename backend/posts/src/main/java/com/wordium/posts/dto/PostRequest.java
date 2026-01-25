// src/main/java/com/wordium/posts/dto/PostRequest.java
package com.wordium.posts.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequest(
                @NotBlank(message = "Title is required") @Size(max = 500, message = "Title cannot exceed 500 characters") String title,

                @NotBlank(message = "Content is required") @Size(max = 1000, message = "Content cannot exceed 1000 characters") String content,

                List<MediaRequest> media

) {
}



