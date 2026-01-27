package com.wordium.posts.services;

// src/main/java/com/wordium/posts/service/PostService.java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wordium.posts.dto.CommentRequest;
import com.wordium.posts.dto.CommentResponse;
import com.wordium.posts.dto.PostReactionRequest;
import com.wordium.posts.dto.PostRequest;
import com.wordium.posts.dto.PostResponse;

public interface PostService {

    // posts
    PostResponse createPost(Long userId, PostRequest request);

    PostResponse getPostById(Long userId,Long id);

    Page<PostResponse> getFeed(Pageable pageable, Long userId);

    Page<PostResponse> getAllposts(Long userId,Pageable pageable);

    Page<PostResponse> getPostsByUser(Long userId, Pageable pageable);

    PostResponse updatePost(Long postId, Long userId, PostRequest request); // only owner or admin

    void deletePost(Long postId, Long userId, String role); // only owner or admin

    void flagPost(Long postId);

    void unflagPost(Long postId);

    // likes
    void react(Long userId, Long postId, PostReactionRequest req);

    // comments
    CommentResponse createComment(Long userId, Long postId, CommentRequest request);

    Page<CommentResponse> getPostComments(Long postId, Pageable pageable);

    void deleteComment(Long userId, String role,Long id, Long commentId);

}
