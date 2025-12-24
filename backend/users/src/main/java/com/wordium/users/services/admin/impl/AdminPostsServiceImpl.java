
package com.wordium.users.services.admin.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wordium.users.client.PostServiceClient;
import com.wordium.users.dto.PostResponse;
import com.wordium.users.services.admin.AdminPostsService;

@Service
public class AdminPostsServiceImpl implements AdminPostsService {

    private final PostServiceClient postServiceClient;

    public AdminPostsServiceImpl(PostServiceClient postServiceClient) {
        this.postServiceClient = postServiceClient;
    }

    @Override
    public List<PostResponse> getAllPosts() {
        return postServiceClient.getAllPosts();
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
}