package com.wordium.users.services.admin;

import org.springframework.data.domain.Pageable;

import com.wordium.users.dto.PaginatedResponse;
import com.wordium.users.dto.posts.PostResponse;

public interface AdminPostsService {
     PaginatedResponse<PostResponse> getAllPosts(Pageable pageable);
    PostResponse getPostById(Long id);
    void deletePost(Long id);
    PostResponse flagPost(Long id);      
    PostResponse unflagPost(Long id);    
}
