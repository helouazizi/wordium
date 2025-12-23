package com.wordium.users.services.admin;

import java.util.List;

import com.wordium.users.dto.ReportResponse;

import feign.form.ContentType;

public interface AdminReportsService {

    List<ReportResponse> getAllReports(Long userId, Long postId, ContentType type);

    ReportResponse getReportById(Long id);

    ReportResponse resolveReport(Long id);

    void deleteReport(Long id);
}
