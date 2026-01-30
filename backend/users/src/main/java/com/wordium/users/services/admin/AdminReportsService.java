package com.wordium.users.services.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wordium.users.dto.PaginatedResponse;
import com.wordium.users.dto.report.ReportPostResponse;
import com.wordium.users.dto.report.UserReportResponse;

public interface AdminReportsService {

    PaginatedResponse<ReportPostResponse> getAllPostReports(Pageable page);

    ReportPostResponse getPostReportById(Long id);

    ReportPostResponse resolvePostReport(Long id);

    void deletePostReport(Long id);

    // void reportUser(Long reporterId, Long reportedUserId, CreateUserReportRequest request);

    Page<UserReportResponse> getAllUserReports(Long viewerId,Pageable pageable);

    UserReportResponse getUserReportById(Long id, Long viewerId);

    void resolveUserReport(Long reportId, Long adminId);

    Long getTotalUserReports();
      Long getTotalPostsReports();

    void deleteUserReport(Long reportId);
}
