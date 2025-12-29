package com.wordium.posts.services.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wordium.posts.dto.ReportPostResponse;
import com.wordium.posts.dto.ReportRequest;
import com.wordium.posts.dto.UserProfile;
import com.wordium.posts.models.Report;
import com.wordium.posts.repo.PostRepository;
import com.wordium.posts.repo.ReportRepository;
import com.wordium.posts.services.ReportService;
import com.wordium.posts.utils.UserEnrichmentHelper;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final UserEnrichmentHelper userEnrichmentHelper;

    public ReportServiceImpl(ReportRepository reportRepository, PostRepository postRepository, UserEnrichmentHelper userEnrichmentHelper) {
        this.reportRepository = reportRepository;
        this.userEnrichmentHelper = userEnrichmentHelper;
        this.postRepository = postRepository;
    }

    @Override
    public ReportPostResponse createReport(Long userId, ReportRequest report) {
        if (!postRepository.existsById(report.reportedPostId())) {
            throw new EntityNotFoundException("Post not found with id: ");
        }
        boolean exists = reportRepository.existsByReporterIdAndReportedPostId(userId, report.reportedPostId());

        if (exists) {
            throw new IllegalStateException("User has already reported this target with the same reason.");
        }

        Report data = new Report();
        data.setReporterId(userId);
        data.setReportedPostId(report.reportedPostId());
        data.setReason(report.reason());
        reportRepository.save(data);

        postRepository.incrementReportCount(report.reportedPostId());

        return mapToResponse(data, new UserProfile(userId, null, null, "null", null, "null", null));
    }

    private ReportPostResponse mapToResponse(Report report, UserProfile reporterProfile) {
        return new ReportPostResponse(
                report.getId(),
                reporterProfile,
                report.getReportedPostId(),
                report.getReason(),
                report.getResolved(),
                new UserProfile(report.getResolvedById(), null, null, "null", null, "null", null), // resolvedBy can be enriched later if needed
                report.getResolvedAt(),
                report.getCreatedAt()
        );
    }

    @Override
    public long countUnresolvedReports() {
        return reportRepository.countByResolvedIsFalse();
    }

    @Override
    public ReportPostResponse resolveReport(Long userId, Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new EntityNotFoundException("Report Not Fount"));
        report.setResolvedAt(LocalDateTime.now());
        report.setResolved(true);
        reportRepository.save(report);

        return mapToResponse(report, new UserProfile(userId, null, null, "null", null, "null", null));
    }

    @Override
    public Page<ReportPostResponse> getReports(Pageable pageable) {
        Page<Report> page = reportRepository.findAll(pageable);
        return userEnrichmentHelper.enrichPage(
                page,
                Report::getReporterId,
                this::mapToResponse);
    }

}
