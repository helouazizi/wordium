package com.wordium.users.services.admin.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wordium.users.client.PostServiceClient;
import com.wordium.users.dto.ReportResponse;
import com.wordium.users.services.admin.AdminReportsService;

import feign.form.ContentType;

@Service
public class AdminReportsServiceImpl implements AdminReportsService {

    private final PostServiceClient postServiceClient;

    public AdminReportsServiceImpl( PostServiceClient postServiceClient) {
        this.postServiceClient = postServiceClient;
    }

    @Override
    public List<ReportResponse> getAllReports(Long userId, Long postId, ContentType type) {
        return postServiceClient.getAllReports(userId, postId, type);
    }

    @Override
    public ReportResponse getReportById(Long id) {
        return postServiceClient.getReportById(id);
    }

    @Override
    public ReportResponse resolveReport(Long id) {
        return postServiceClient.resolveReport(id);
    }

    @Override
    public void deleteReport(Long id) {
        postServiceClient.deleteReport(id);
    }
}