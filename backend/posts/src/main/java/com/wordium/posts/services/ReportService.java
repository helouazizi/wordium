package com.wordium.posts.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wordium.posts.dto.ReportRequest;
import com.wordium.posts.dto.ReportResponse;

public interface ReportService {

    ReportResponse createReport(Long userId, ReportRequest report);

    Page<ReportResponse> getReports(Pageable pageable);

    long countUnresolvedReports();

    ReportResponse resolveReport(Long reportId, Long resolverId);

}
