package com.wordium.users.controllers.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.users.dto.PostResponse;
import com.wordium.users.services.admin.AdminPostService;

@RestController
@RequestMapping("/users/admin/posts")
public class AdminPostsController {

    private final AdminPostService adminPostService;

    public AdminPostsController(AdminPostService adminPostService) {
        this.adminPostService = adminPostService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(
            @RequestParam(required = false) Boolean reported) {
        List<PostResponse> posts = adminPostService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        PostResponse post = adminPostService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        adminPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/flag")
    public ResponseEntity<PostResponse> flagPost(@PathVariable Long id) {
        PostResponse post = adminPostService.flagPost(id);
        return ResponseEntity.ok(post);
    }

    @PatchMapping("/{id}/unflag")
    public ResponseEntity<PostResponse> unflagPost(@PathVariable Long id) {
        PostResponse post = adminPostService.unflagPost(id);
        return ResponseEntity.ok(post);
    }
}