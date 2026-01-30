package com.wordium.posts.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wordium.posts.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);

    void deleteByIdAndUserId(Long commentId, Long userId);

    Optional<Comment> findByIdAndPostId(Long id, Long postId);

    void deleteAllByUserId(Long userId);

}
