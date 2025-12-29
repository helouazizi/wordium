package com.wordium.posts.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
            @Valid PaginationRequest paginationRequest
    ) {
        Pageable pageable = paginationRequest.toPageable();
        var page = reportService.getReports(pageable);
        return ResponseEntity.ok(PaginatedResponse.fromPage(page));
    }

    @GetMapping("/unresolved/count")
    public ResponseEntity<Long> countUnresolvedReports() {
        return ResponseEntity.ok(reportService.countUnresolvedReports());
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<Void> resolveReport(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long id
    ) {
        reportService.resolveReport(userId, id);
        return ResponseEntity.ok().build();
    }
}
