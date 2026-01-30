package com.wordium.posts.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.posts.dto.PaginatedResponse;
import com.wordium.posts.dto.PaginationRequest;
import com.wordium.posts.dto.ReportPostResponse;
import com.wordium.posts.services.ReportService;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;

@Hidden
@RestController
@RequestMapping("posts/internal/reports")
public class ReportsInternalController {

    private final ReportService reportService;

    public ReportsInternalController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<ReportPostResponse>> getReports(
            @Valid PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        var page = reportService.getReports(pageable);
        return ResponseEntity.ok(PaginatedResponse.fromPage(page));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> reportsCount() {
        return ResponseEntity.ok(reportService.reportsCount());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportPostResponse> getReport(
            @PathVariable Long id) {
        ReportPostResponse report = reportService.getReport(id);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/unresolved/count")
    public ResponseEntity<Long> countUnresolvedReports() {
        return ResponseEntity.ok(reportService.countUnresolvedReports());
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Void> resolveReport(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long id) {
        reportService.resolveReport(userId, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok().build();
    }
}
