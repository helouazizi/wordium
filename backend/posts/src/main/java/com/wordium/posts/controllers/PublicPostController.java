// src/main/java/com/wordium/posts/controllers/PublicPostController.java
package com.wordium.posts.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.posts.dto.PaginatedResponse;
import com.wordium.posts.dto.PostRequest;
import com.wordium.posts.dto.PostResponse;
import com.wordium.posts.services.impl.PostServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/posts")
public class PublicPostController {

    private final PostServiceImpl postService;

    public PublicPostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestHeader("User-Id") Long userId,
            @Valid @RequestBody PostRequest request) {
        PostResponse post = postService.createPost(userId, request);
        return ResponseEntity.status(201).body(post);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<PostResponse>> getFeed(Pageable pageable) {
        var page = postService.getFeed(pageable);
        return ResponseEntity.ok(PaginatedResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }
}