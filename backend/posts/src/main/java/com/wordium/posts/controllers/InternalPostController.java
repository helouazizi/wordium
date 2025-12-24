// src/main/java/com/wordium/posts/controllers/InternalPostController.java
package com.wordium.posts.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.posts.dto.PaginatedResponse;
import com.wordium.posts.dto.PostResponse;
import com.wordium.posts.services.impl.PostServiceImpl;

@RestController
@RequestMapping("posts/internal/posts")
public class InternalPostController {

    private final PostServiceImpl postService;

    public InternalPostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<PostResponse>> getAllPosts(Pageable pageable) {
        var page = postService.getPosts(pageable);  // includes flagged/reported
        return ResponseEntity.ok(PaginatedResponse.fromPage(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id, null);  // admin call
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/flag")
    public ResponseEntity<PostResponse> flagPost(@PathVariable Long id) {
        postService.flagPost(id);
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PatchMapping("/{id}/unflag")
    public ResponseEntity<PostResponse> unflagPost(@PathVariable Long id) {
        postService.unflagPost(id);
        return ResponseEntity.ok(postService.getPostById(id));
    }
}




//  public Page<PostResponse> getPosts(Pageable pageable) {
//     Page<Post> postPage = postRepository.findAll(pageable);  // or with Specification for filters

//     // Map entities to DTOs
//     List<PostResponse> dtos = postPage.getContent().stream()
//         .map(postMapper::toResponse)
//         .toList();

//     return new PageImpl<>(dtos, pageable, postPage.getTotalElements());
// }