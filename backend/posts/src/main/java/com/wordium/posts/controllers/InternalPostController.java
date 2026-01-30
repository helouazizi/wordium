// src/main/java/com/wordium/posts/controllers/InternalPostController.java
package com.wordium.posts.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.posts.dto.PaginatedResponse;
import com.wordium.posts.dto.PaginationRequest;
import com.wordium.posts.dto.PostResponse;
import com.wordium.posts.services.impl.PostServiceImpl;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;

@Hidden
@RestController
@RequestMapping("posts/internal/posts")
public class InternalPostController {

    private final PostServiceImpl postService;

    public InternalPostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<PostResponse>> getAllPosts(@Valid PaginationRequest paginationRequest,
            @RequestHeader("User-Id") Long userId) {
        Pageable pageable = paginationRequest.toPageable();
        var page = postService.getAllposts(userId, pageable);
        return ResponseEntity.ok(PaginatedResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id, @RequestHeader("User-Id") Long userId) {
        return ResponseEntity.ok(postService.getPostById(userId, id));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> postsCount() {
        return ResponseEntity.ok(postService.postsCount());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@RequestHeader("User-Id") Long userId,
            @RequestHeader("User-Role") String role, @PathVariable Long id) {
        postService.deletePost(id, userId, role);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/flag")
    public ResponseEntity<Void> flagPost(@PathVariable Long id) {
        postService.flagPost(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/unflag")
    public ResponseEntity<Void> unflagPost(@PathVariable Long id) {
        postService.unflagPost(id);
        return ResponseEntity.noContent().build();
    }
}
