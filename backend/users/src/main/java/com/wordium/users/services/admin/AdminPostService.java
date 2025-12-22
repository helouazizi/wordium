package com.wordium.users.services.admin;

import java.util.List;

import com.wordium.users.dto.PostResponse;

public interface AdminPostService {
    List<PostResponse> getAllPosts();
    PostResponse getPostById(Long id);
    void deletePost(Long id);
    PostResponse flagPost(Long id);      
    PostResponse unflagPost(Long id);    
}
