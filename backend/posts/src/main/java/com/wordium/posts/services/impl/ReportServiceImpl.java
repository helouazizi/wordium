package com.wordium.posts.services.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wordium.posts.dto.ReportRequest;
import com.wordium.posts.dto.ReportResponse;
import com.wordium.posts.dto.UserProfile;
import com.wordium.posts.models.Report;
import com.wordium.posts.repo.ReportRepository;
import com.wordium.posts.services.ReportService;
import com.wordium.posts.utils.UserEnrichmentHelper;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserEnrichmentHelper userEnrichmentHelper;

    public ReportServiceImpl(ReportRepository reportRepository, UserEnrichmentHelper userEnrichmentHelper) {
        this.reportRepository = reportRepository;
        this.userEnrichmentHelper = userEnrichmentHelper;
    }

    @Override
    public ReportResponse createReport(Long userId, ReportRequest report) {
        boolean exists = report.reportedPostId() != null
                ? reportRepository.existsByReporterIdAndReportedPostId(report.reporterId(), report.reportedPostId())
                : reportRepository.existsByReporterIdAndReportedUserId(report.reporterId(), report.reportedUserId());

        if (exists) {
            throw new IllegalStateException("User has already reported this target with the same reason.");
        }

        Report data = new Report();
        data.setReporterId(userId);
        data.setReportedPostId(report.reportedPostId());
        data.setReportedUserId(report.reportedUserId());
        data.setReason(report.reason());
        reportRepository.save(data);

        return mapToResponse(data, new UserProfile(userId, null, null, "null", null, "null", null));
    }

    private ReportResponse mapToResponse(Report report, UserProfile reporterProfile) {
        return new ReportResponse(
                report.getId(),
                reporterProfile,
                report.getReportedPostId(),
                new UserProfile(report.getReportedUserId(), null, null, "null", null, "null", null), // reported user profile
                report.getReason(),
                new UserProfile(report.getResolvedById(), null, null, "null", null, "null", null), // resolvedBy can be enriched later if needed
                report.getResolvedAt(),
                report.getCreatedAt()
        );
    }

    @Override
    public long countUnresolvedReports() {
        return reportRepository.countByResolvedAtIsNull();
    }

    @Override
    public ReportResponse resolveReport(Long userId, Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new EntityNotFoundException("Report Not Fount"));
        report.setResolvedAt(LocalDateTime.now());
        report.setResolved(true);
        reportRepository.save(report);

        return mapToResponse(report, new UserProfile(userId, null, null, "null", null, "null", null));
    }

    @Override
    public Page<ReportResponse> getReports(Pageable pageable) {
        Page<Report> page = reportRepository.findAll(pageable);
        return userEnrichmentHelper.enrichPage(
                page,
                Report::getReporterId,
                this::mapToResponse);
    }

}
