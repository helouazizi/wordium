// src/main/java/com/wordium/posts/dto/PostImageResponse.java
package com.wordium.posts.dto;

public record PostImageResponse(
    Long id,
    String url,
    String altText,
    int displayOrder
) {}