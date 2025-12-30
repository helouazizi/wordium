package com.wordium.users.services.admin;

import org.springframework.data.domain.Pageable;

import com.wordium.users.dto.PaginatedResponse;
import com.wordium.users.dto.report.ReportPostResponse;

public interface AdminReportsService {

    PaginatedResponse<ReportPostResponse> getAllReports(Pageable page);

    ReportPostResponse getReportById(Long id);

    ReportPostResponse resolveReport(Long id);

    void deleteReport(Long id);
}
