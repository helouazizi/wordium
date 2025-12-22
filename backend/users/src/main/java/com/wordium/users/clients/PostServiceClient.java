package com.wordium.users.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.wordium.users.dto.PostResponse;

@FeignClient(name = "posts-service", url = "http://localhost:8088") // or use service discovery
public interface PostServiceClient {

    @GetMapping("/posts")
    List<PostResponse> getAllPosts(@RequestParam(required = false) Boolean reported);

    @GetMapping("/posts/{id}")
    PostResponse getPostById(@PathVariable("id") Long id);

    @DeleteMapping("/posts/{id}")
    void deletePost(@PathVariable("id") Long id);

    @PatchMapping("/posts/{id}/flag")
    PostResponse flagPost(@PathVariable("id") Long id);

    @PatchMapping("/posts/{id}/unflag")
    PostResponse unflagPost(@PathVariable("id") Long id);
}