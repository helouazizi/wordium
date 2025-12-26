package com.wordium.posts.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wordium.posts.dto.PaginatedResponse;
import com.wordium.posts.dto.ReportRequest;
import com.wordium.posts.dto.ReportResponse;
import com.wordium.posts.dto.UserProfile;
import com.wordium.posts.models.Report;
import com.wordium.posts.repo.ReportRepository;
import com.wordium.posts.services.ReportService;
import com.wordium.posts.utils.UserEnrichmentHelper;

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
    public Report createReport(ReportRequest report) {
        boolean exists = report.reportedPostId() != null
                ? reportRepository.existsByReporterIdAndReportedPostIdAndReason(report.reporterId(), report.reportedPostId(), report.reason())
                : reportRepository.existsByReporterIdAndReportedUserIdAndReason(report.reporterId(), report.reportedUserId(), report.reason());

        if (exists) {
            throw new IllegalStateException("User has already reported this target with the same reason.");
        }

        Report data = new Report();
        data.setReporterId(report.reporterId());
        data.setReportedPostId(report.reportedPostId());
        data.setReportedUserId(report.reportedUserId());
        data.setReason(report.reason());

        return reportRepository.save(data);
    }

    private ReportResponse mapToResponse(Report report, UserProfile reporterProfile) {
        // Only reporterProfile is enriched via helper
        return new ReportResponse(
                report.getId(),
                reporterProfile,
                report.getReportedPostId(),
                null,  // reported user can be enriched later if needed
                report.getReason(),
                null,  // resolvedBy can be enriched later if needed
                report.getResolvedAt(),
                report.getCreatedAt()
        );
    }

    private PaginatedResponse<ReportResponse> mapPage(Page<Report> page) {
        Page<ReportResponse> enrichedPage = userEnrichmentHelper.enrichPage(
                page,
                Report::getReporterId,
                this::mapToResponse
        );
        return PaginatedResponse.fromPage(enrichedPage);
    }

    // @Override
    // public ReportResponse getReportById(Long reportId) {
    //     Report report = reportRepository.findById(reportId)
    //             .orElseThrow(() -> new EntityNotFoundException("Report not found with id: " + reportId));
    //     return userEnrichmentHelper.enrichSingle(
    //             report,
    //             Report::getReporterId,
    //             this::mapToResponse
    //     );
    // }

    @Override
    public PaginatedResponse<ReportResponse> getReportsByReporter(Long reporterId, Pageable pageable) {
        return mapPage(reportRepository.findByReporterId(reporterId, pageable));
    }

    @Override
    public PaginatedResponse<ReportResponse> getReportsAgainstUser(Long reportedUserId, Pageable pageable) {
        return mapPage(reportRepository.findByReportedUserId(reportedUserId, pageable));
    }

    @Override
    public PaginatedResponse<ReportResponse> getReportsAgainstPost(Long reportedPostId, Pageable pageable) {
        return mapPage(reportRepository.findByReportedPostId(reportedPostId, pageable));
    }

    @Override
    public PaginatedResponse<ReportResponse> getUnresolvedReports(Pageable pageable) {
        return mapPage(reportRepository.findByResolvedAtIsNull(pageable));
    }

    @Override
    public PaginatedResponse<ReportResponse> getResolvedReports(Pageable pageable) {
        return mapPage(reportRepository.findByResolvedAtIsNotNull(pageable));
    }

    @Override
    public PaginatedResponse<ReportResponse> getUnresolvedReportsByReporter(Long reporterId, Pageable pageable) {
        return mapPage(reportRepository.findByReporterIdAndResolvedAtIsNull(reporterId, pageable));
    }

    @Override
    public PaginatedResponse<ReportResponse> getUnresolvedReportsAgainstUser(Long reportedUserId, Pageable pageable) {
        return mapPage(reportRepository.findByReportedUserIdAndResolvedAtIsNull(reportedUserId, pageable));
    }

    @Override
    public PaginatedResponse<ReportResponse> getUnresolvedReportsAgainstPost(Long reportedPostId, Pageable pageable) {
        return mapPage(reportRepository.findByReportedPostIdAndResolvedAtIsNull(reportedPostId, pageable));
    }

    @Override
    public boolean hasUserReportedPost(Long reporterId, Long reportedPostId, String reason) {
        return reportRepository.existsByReporterIdAndReportedPostIdAndReason(reporterId, reportedPostId, reason);
    }

    @Override
    public boolean hasUserReportedUser(Long reporterId, Long reportedUserId, String reason) {
        return reportRepository.existsByReporterIdAndReportedUserIdAndReason(reporterId, reportedUserId, reason);
    }

    @Override
    public long countReportsByReporter(Long reporterId) {
        return reportRepository.countByReporterId(reporterId);
    }

    @Override
    public long countUnresolvedReports() {
        return reportRepository.countByResolvedAtIsNull();
    }

    @Override
    public Optional<ReportResponse> resolveReport(Long reportId, Long resolverId) {
        return reportRepository.findById(reportId)
                .map(report -> {
                    report.setResolvedById(resolverId);
                    report.setResolvedAt(LocalDateTime.now());
                    Report saved = reportRepository.save(report);
                    return userEnrichmentHelper.enrichSingle(saved, Report::getReporterId, this::mapToResponse);
                });
    }

    @Override
    public PaginatedResponse<ReportResponse> getLatestReports(Pageable pageable) {
        return mapPage(reportRepository.findAll(pageable));
    }
}
