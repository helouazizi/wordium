package com.wordium.posts.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wordium.posts.models.PostReaction;

@Repository
public interface ReactionRepository extends JpaRepository<PostReaction, Long> {

     Optional<PostReaction> findByPostIdAndUserId(Long userId,Long postId );
}
