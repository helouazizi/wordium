package com.wordium.posts.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wordium.posts.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostIdOrderByCreatedAtAsc(Long postId, Pageable pageable);

    void deleteByIdAndUserId(Long commentId, Long userId);
}
