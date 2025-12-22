package com.wordium.users.services.admin.impl;

import java.util.List;

import com.wordium.users.clients.PostServiceClient;
import com.wordium.users.dto.PostResponse;
import com.wordium.users.services.admin.AdminPostService;

public class AdminPostServiceImpl implements AdminPostService {

    private final PostServiceClient postClient; 

    public AdminPostServiceImpl(PostServiceClient postClient) {
        this.postClient = postClient;
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return postClient.getAllPosts(true);
    }

    @Override
    public PostResponse getPostById(Long id) {
        return postClient.getPostById(id);
    }

    @Override
    public void deletePost(Long id) {
        postClient.deletePost(id);
    }

    @Override
    public PostResponse flagPost(Long id) {
        return postClient.flagPost(id);
    }

    @Override
    public PostResponse unflagPost(Long id) {
        return postClient.unflagPost(id);
    }

}
