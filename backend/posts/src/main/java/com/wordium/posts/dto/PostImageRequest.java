package com.wordium.posts.dto;

public record PostImageRequest(
        String url,
        String altText,
        Integer displayOrder
        ) {

}
