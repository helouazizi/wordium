package com.wordium.posts.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wordium.posts.dto.ReportPostResponse;
import com.wordium.posts.dto.ReportRequest;

public interface ReportService {

    ReportPostResponse createReport(Long userId, Long id, ReportRequest report);

    Page<ReportPostResponse> getReports(Pageable pageable);

    ReportPostResponse getReport(Long id);

    long countUnresolvedReports();

    ReportPostResponse resolveReport(Long reportId, Long resolverId);

}
