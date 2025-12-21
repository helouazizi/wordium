package com.wordium.users.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wordium.users.models.Followers;

@Repository
public interface FollowersRepo extends JpaRepository<Followers, Long> {
    List<Followers> findByFollowerId(Long followerId);
    List<Followers> findByFollowedId(Long followedId);
    Optional<Followers> findByFollowerIdAndFollowedId(Long followerId , Long followedId);
    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);
}
