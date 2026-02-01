package com.wordium.users.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.wordium.users.models.Followers;

@Repository
public interface FollowersRepo extends JpaRepository<Followers, Long> {
    Page<Followers> findByFollowerId(Long followerId, Pageable pageable);

    Page<Followers> findByFollowedId(Long followedId, Pageable pageable);

    Optional<Followers> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);

    @Query("""
                select f.followerId
                from Followers f
                where f.followedId = :userId
            """)
    Page<Long> findFollowerIdsByFollowedId(Long userId, Pageable pageable);

    long countByFollowedId(Long userId); // followers count

    long countByFollowerId(Long userId); // following count
}
