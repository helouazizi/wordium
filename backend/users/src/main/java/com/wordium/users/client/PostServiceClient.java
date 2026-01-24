package com.wordium.users.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.wordium.users.config.FeignClientConfig;
import com.wordium.users.dto.PaginatedResponse;
import com.wordium.users.dto.posts.PostResponse;
import com.wordium.users.dto.report.ReportPostResponse;

@FeignClient(name = "posts-service", url = "${services.api.url}/api/v1/posts", configuration = FeignClientConfig.class)
public interface PostServiceClient {

        @GetMapping("/internal/posts")
        PaginatedResponse<PostResponse> getAllPosts(
                        @SpringQueryMap Pageable pageable);

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
        PaginatedResponse<ReportPostResponse> getAllReports(
                        @SpringQueryMap Pageable pageable);

        @GetMapping("/internal/reports/{id}")
        ReportPostResponse getReportById(@PathVariable Long id);

        @PatchMapping("/internal/reports/{id}/resolve")
        ReportPostResponse resolveReport(@PathVariable Long id);

        @DeleteMapping("/internal/reports/{id}")
        void deleteReport(@PathVariable Long id);

        @GetMapping("/internal/posts/stats/{userId}")
        ReportPostResponse getUserStats(@PathVariable("userId") Long userId);

}
