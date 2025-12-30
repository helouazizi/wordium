package com.wordium.users.services.admin.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wordium.users.client.PostServiceClient;
import com.wordium.users.dto.PaginatedResponse;
import com.wordium.users.dto.report.ReportPostResponse;
import com.wordium.users.services.admin.AdminReportsService;

import feign.form.ContentType;

@Service
public class AdminReportsServiceImpl implements AdminReportsService {

    private final PostServiceClient postServiceClient;

    public AdminReportsServiceImpl( PostServiceClient postServiceClient) {
        this.postServiceClient = postServiceClient;
    }

    @Override
    public PaginatedResponse<ReportPostResponse> getAllReports(Pageable page) {
        return postServiceClient.getAllReports(page);
    }

    @Override
    public ReportPostResponse getReportById(Long id) {
        return postServiceClient.getReportById(id);
    }

    @Override
    public ReportPostResponse resolveReport(Long id) {
        return postServiceClient.resolveReport(id);
    }

    @Override
    public void deleteReport(Long id) {
        postServiceClient.deleteReport(id);
    }
}