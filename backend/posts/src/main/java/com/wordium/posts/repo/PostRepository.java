package com.wordium.posts.repo;

// src/main/java/com/wordium/posts/repository/PostRepository.java

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wordium.posts.models.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    // Find posts by user
    Page<Post> findByUserId(Long userId, Pageable pageable);

    // Optional: custom query for public feed (not flagged)
    Page<Post> findByFlaggedFalse(Pageable pageable);

    // Increment likes/comments/report count atomically
    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.likesCount = p.likesCount + 1 WHERE p.id = :postId")
    void incrementLikesCount(Long postId);

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.commentsCount = p.commentsCount + 1 WHERE p.id = :postId")
    void incrementCommentsCount(Long postId);

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.reportCount = p.reportCount + 1, p.reported = true WHERE p.id = :postId")
    void incrementReportCount(Long postId);
}
