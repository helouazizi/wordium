package com.wordium.posts.repo;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wordium.posts.models.PostReaction;

import feign.Param;

@Repository
public interface ReactionRepository extends JpaRepository<PostReaction, Long> {

     Optional<PostReaction> findByPostIdAndUserId(Long userId, Long postId);

     @Query("SELECT r.postId FROM PostReaction r WHERE r.userId = :userId AND r.postId IN :postIds")
     Set<Long> findLikedPostIdsByUserIdAndPostIdIn(@Param("userId") Long userId,
               @Param("postIds") Collection<Long> postIds);
}
