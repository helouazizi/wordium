package com.wordium.posts.services.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wordium.posts.dto.ReportPostResponse;
import com.wordium.posts.dto.ReportRequest;
import com.wordium.posts.dto.UserProfile;
import com.wordium.posts.exeptions.ConflictException;
import com.wordium.posts.exeptions.NotFoundException;
import com.wordium.posts.models.Report;
import com.wordium.posts.repo.PostRepository;
import com.wordium.posts.repo.ReportRepository;
import com.wordium.posts.services.ReportService;
import com.wordium.posts.utils.UserEnrichmentHelper;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final PostRepository postRepository;
    private final UserEnrichmentHelper userEnrichmentHelper;

    public ReportServiceImpl(ReportRepository reportRepository, PostRepository postRepository,
            UserEnrichmentHelper userEnrichmentHelper) {
        this.reportRepository = reportRepository;
        this.userEnrichmentHelper = userEnrichmentHelper;
        this.postRepository = postRepository;
    }

    @Override
    public ReportPostResponse createReport(Long userId, Long posId, ReportRequest report) {
        if (!postRepository.existsById(posId)) {
            throw new NotFoundException("Report not found");
        }
        boolean exists = reportRepository.existsByReporterIdAndReportedPostId(userId, posId);

        if (exists) {
            throw new ConflictException("You have already reported this post");
        }

        Report data = new Report();
        data.setReporterId(userId);
        data.setReportedPostId(posId);
        data.setReason(report.reason());
        reportRepository.save(data);

        postRepository.incrementReportCount(posId);

        return mapToResponse(data, new UserProfile(userId, null, null, "null", null, "null", null,null,null,null,null,null,null,null,null,null,null,null));
    }

    private ReportPostResponse mapToResponse(Report report, UserProfile reporterProfile) {
        return new ReportPostResponse(
                report.getId(),
                reporterProfile,
                report.getReportedPostId(),
                report.getReason(),
                report.getResolved(),
                report.getResolvedAt(),
                report.getCreatedAt());
    }

    @Override
    public long countUnresolvedReports() {
        return reportRepository.countByResolvedIsFalse();
    }

    @Override
    public ReportPostResponse resolveReport(Long userId, Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new NotFoundException("Report not found"));
        report.setResolvedAt(LocalDateTime.now());
        report.setResolved(true);
        reportRepository.save(report);

        return mapToResponse(report, new UserProfile(userId, null, null, "null", null, "null", null,null,null,null,null,null,null,null,null,null,null,null));
    }

    @Override
    public Page<ReportPostResponse> getReports(Pageable pageable) {
        Page<Report> page = reportRepository.findAll(pageable);
        return userEnrichmentHelper.enrichPage(
                page,
                Report::getReporterId,
                this::mapToResponse);
    }

    @Override
    public ReportPostResponse getReport(Long id) {
        Report page = reportRepository.findById(id).orElseThrow(() -> new NotFoundException("Report not found"));
        return userEnrichmentHelper.enrichSingle(
                page,
                Report::getReporterId,
                this::mapToResponse);
    }

}
