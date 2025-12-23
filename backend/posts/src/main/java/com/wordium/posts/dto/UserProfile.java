package com.wordium.posts.dto;


public  record UserProfile(
    Long id,
    String username,
    String displayName,
    String avatarUrl
) {}