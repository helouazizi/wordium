package com.wordium.posts.services;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.wordium.posts.dto.PaginatedResponse;
import com.wordium.posts.dto.ReportRequest;
import com.wordium.posts.dto.ReportResponse;
import com.wordium.posts.models.Report;

public interface ReportService {

    // Create a new report (throws exception if duplicate)
    Report createReport(ReportRequest report);

    // Fetch paginated reports submitted by a specific user
    PaginatedResponse<ReportResponse> getReportsByReporter(Long reporterId, Pageable pageable);

    // Fetch paginated reports against a specific user
    PaginatedResponse<ReportResponse> getReportsAgainstUser(Long reportedUserId, Pageable pageable);

    // Fetch paginated reports against a specific post
    PaginatedResponse<ReportResponse> getReportsAgainstPost(Long reportedPostId, Pageable pageable);

    // Fetch paginated unresolved reports
    PaginatedResponse<ReportResponse> getUnresolvedReports(Pageable pageable);

    // Fetch paginated resolved reports
    PaginatedResponse<ReportResponse> getResolvedReports(Pageable pageable);

    // Fetch paginated unresolved reports submitted by a user
    PaginatedResponse<ReportResponse> getUnresolvedReportsByReporter(Long reporterId, Pageable pageable);

    // Fetch paginated unresolved reports against a user
    PaginatedResponse<ReportResponse> getUnresolvedReportsAgainstUser(Long reportedUserId, Pageable pageable);

    // Fetch paginated unresolved reports against a post
    PaginatedResponse<ReportResponse> getUnresolvedReportsAgainstPost(Long reportedPostId, Pageable pageable);

    // Check if a user already reported a post or user with the same reason
    boolean hasUserReportedPost(Long reporterId, Long reportedPostId, String reason);
    boolean hasUserReportedUser(Long reporterId, Long reportedUserId, String reason);

    // Count total reports by a user
    long countReportsByReporter(Long reporterId);

    // Count unresolved reports
    long countUnresolvedReports();

    // Resolve a report (set resolvedById and resolvedAt)
    Optional<ReportResponse> resolveReport(Long reportId, Long resolverId);

    // Fetch latest N reports (paginated)
    PaginatedResponse<ReportResponse> getLatestReports(Pageable pageable);

}
