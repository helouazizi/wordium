package com.wordium.posts.services;

// src/main/java/com/wordium/posts/service/PostService.java

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wordium.posts.dto.PostRequest;
import com.wordium.posts.dto.PostResponse;

public interface PostService {

    PostResponse createPost(Long userId, PostRequest request);

    PostResponse getPostById(Long id);

    Page<PostResponse> getFeed(Pageable pageable);

    Page<PostResponse> getPostsByUser(Long userId, Pageable pageable);

    PostResponse updatePost(Long postId, Long userId, PostRequest request);  // only owner or admin

    void deletePost(Long postId, Long userId);  // only owner or admin

    void flagPost(Long postId);

    void unflagPost(Long postId);

    // Admin/internal
    void incrementLikes(Long postId);
    void incrementComments(Long postId);
    void incrementReports(Long postId);
}
