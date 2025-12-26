package com.wordium.posts.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.wordium.posts.models.Report;

public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {

    // Single-entity queries
    Optional<Report> findByReporterIdAndReportedPostIdAndReason(Long reporterId, Long reportedPostId, String reason);
    Optional<Report> findByReporterIdAndReportedUserIdAndReason(Long reporterId, Long reportedUserId, String reason);

    // Paginated queries for multiple results
    Page<Report> findByReporterId(Long reporterId, Pageable pageable);
    Page<Report> findByReportedUserId(Long reportedUserId, Pageable pageable);
    Page<Report> findByReportedPostId(Long reportedPostId, Pageable pageable);
    Page<Report> findByResolvedAtIsNull(Pageable pageable);
    Page<Report> findByResolvedAtIsNotNull(Pageable pageable);
    Page<Report> findByReporterIdAndResolvedAtIsNull(Long reporterId, Pageable pageable);
    Page<Report> findByReportedUserIdAndResolvedAtIsNull(Long reportedUserId, Pageable pageable);
    Page<Report> findByReportedPostIdAndResolvedAtIsNull(Long reportedPostId, Pageable pageable);
    Page<Report> findByResolvedById(Long resolvedById, Pageable pageable);

    // Existence and count checks (no change needed)
    long countByReporterId(Long reporterId);
    long countByResolvedAtIsNull();
    boolean existsByReporterIdAndReportedPostIdAndReason(Long reporterId, Long reportedPostId, String reason);
    boolean existsByReporterIdAndReportedUserIdAndReason(Long reporterId, Long reportedUserId, String reason);
}
