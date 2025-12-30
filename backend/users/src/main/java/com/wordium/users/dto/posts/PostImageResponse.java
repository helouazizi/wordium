package com.wordium.users.dto.posts;

public record PostImageResponse(
    Long id,
    String url,
    String altText,
    int displayOrder
) {}