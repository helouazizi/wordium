// src/main/java/com/wordium/users/controllers/admin/AdminReportsController.java
package com.wordium.users.controllers.admin;

import java.util.List;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.users.dto.ReportResponse;
import com.wordium.users.services.admin.AdminReportsService;

import feign.form.ContentType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin - Reports Management", description = "Admin endpoints for viewing and managing user reports on posts/users")
@RestController
@RequestMapping("/users/admin/reports")
public class AdminReportsController {

    private final AdminReportsService adminReportsService;

    public AdminReportsController(AdminReportsService adminReportsService) {
        this.adminReportsService = adminReportsService;
    }

    @Operation(
            summary = "List all reports",
            description = "Retrieve all reports with optional filters: by reported user, post, or content type (POST, USER, COMMENT)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reports fetched successfully",
                    content = @Content(schema = @Schema(implementation = ReportResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not an admin",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    public ResponseEntity<List<ReportResponse>> getAllReports(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long postId,
            @RequestParam(required = false) ContentType type) {

        List<ReportResponse> reports = adminReportsService.getAllReports(userId, postId, type);
        return ResponseEntity.ok(reports);
    }

    @Operation(
            summary = "Get report details by ID",
            description = "Retrieve full details of a specific report."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Report fetched successfully",
                    content = @Content(schema = @Schema(implementation = ReportResponse.class))),
            @ApiResponse(responseCode = "404", description = "Report not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable Long id) {
        ReportResponse report = adminReportsService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    @Operation(
            summary = "Resolve a report",
            description = "Mark a report as resolved. Typically done after taking action (e.g., deleting post, banning user)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Report resolved successfully",
                    content = @Content(schema = @Schema(implementation = ReportResponse.class))),
            @ApiResponse(responseCode = "404", description = "Report not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping("/{id}/resolve")
    public ResponseEntity<ReportResponse> resolveReport(@PathVariable Long id) {
        ReportResponse report = adminReportsService.resolveReport(id);
        return ResponseEntity.ok(report);
    }

    @Operation(
            summary = "Delete a report",
            description = "Permanently delete a report (e.g., if it was a false report or already handled externally)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Report deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Report not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        adminReportsService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}