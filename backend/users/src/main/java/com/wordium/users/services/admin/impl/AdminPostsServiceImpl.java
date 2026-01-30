package com.wordium.users.services.admin.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wordium.users.client.PostServiceClient;
import com.wordium.users.dto.PaginatedResponse;
import com.wordium.users.dto.posts.PostResponse;
import com.wordium.users.services.admin.AdminPostsService;

@Service
public class AdminPostsServiceImpl implements AdminPostsService {

    private final PostServiceClient postServiceClient;

    public AdminPostsServiceImpl(PostServiceClient postServiceClient) {
        this.postServiceClient = postServiceClient;
    }

    @Override
    public PaginatedResponse<PostResponse> getAllPosts(Pageable pageable) {
        return postServiceClient.getAllPosts(pageable);
    }

    @Override
    public PostResponse getPostById(Long id) {
        return postServiceClient.getPostById(id);
    }

    @Override
    public void deletePost(Long id) {
        postServiceClient.deletePost(id);
    }

    @Override
    public PostResponse flagPost(Long id) {
        return postServiceClient.flagPost(id);
    }

    @Override
    public PostResponse unflagPost(Long id) {
        return postServiceClient.unflagPost(id);
    }

    @Override
    public Long getTotalPostsReports() {
        return postServiceClient.postsCount();
    }
}
