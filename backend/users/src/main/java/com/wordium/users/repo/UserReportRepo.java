package com.wordium.users.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.wordium.users.models.UserReport;

import feign.Param;

public interface UserReportRepo extends JpaRepository<UserReport, Long> {

    Page<UserReport> findAll(Pageable pageable);

    Page<UserReport> findByReportedUser_Id(Long userId, Pageable pageable);

    long countByReportedUser_Id(Long userId);

    boolean existsByReportedBy_IdAndReportedUser_Id(Long reporterId, Long reportedUserId);

    void deleteByReportedUser_Id(Long userId);

    void deleteByReportedBy_Id(Long userId);

    @Modifying
    @Query("UPDATE UserReport r SET r.resolvedBy = null WHERE r.resolvedBy.id = :userId")
    void clearResolvedBy(@Param("userId") Long userId);
}
