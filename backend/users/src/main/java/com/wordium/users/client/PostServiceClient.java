package com.wordium.users.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.wordium.users.dto.PostResponse;
import com.wordium.users.dto.ReportResponse;

import feign.form.ContentType;

@FeignClient(name = "posts-service", url = "http://localhost:3003")  // or use service discovery
public interface PostServiceClient {

    @GetMapping("/internal/posts")
    List<PostResponse> getAllPosts();

    @GetMapping("/internal/posts/{id}")
    PostResponse getPostById(@PathVariable("id") Long id);

    @DeleteMapping("/internal/posts/{id}")
    void deletePost(@PathVariable("id") Long id);

    @PatchMapping("/internal/posts/{id}/flag")
    PostResponse flagPost(@PathVariable("id") Long id);

    @PatchMapping("/internal/posts/{id}/unflag")
    PostResponse unflagPost(@PathVariable("id") Long id);

    // reports 
    @GetMapping("/internal/reports")
    List<ReportResponse> getAllReports(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long postId,
            @RequestParam(required = false) ContentType type
    );

    @GetMapping("/internal/reports/{id}")
    ReportResponse getReportById(@PathVariable Long id);

    @PatchMapping("/internal/reports/{id}/resolve")
    ReportResponse resolveReport(@PathVariable Long id);

    @DeleteMapping("/internal/reports/{id}")
    void deleteReport(@PathVariable Long id);
}
