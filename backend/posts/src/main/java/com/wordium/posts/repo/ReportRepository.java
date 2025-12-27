package com.wordium.posts.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.wordium.posts.models.Report;

public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {

    Optional<Report> findByReporterIdAndReportedPostIdAndReason(Long reporterId, Long reportedPostId, String reason);

    Optional<Report> findByReporterIdAndReportedUserIdAndReason(Long reporterId, Long reportedUserId, String reason);

    long countByResolvedAtIsNull();

    boolean existsByReporterIdAndReportedPostId(Long reporterId, Long reportedPostId);

    boolean existsByReporterIdAndReportedUserId(Long reporterId, Long reportedUserId);
}
