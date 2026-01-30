package com.wordium.users.controllers.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.users.dto.PaginatedResponse;
import com.wordium.users.dto.PaginationRequest;
import com.wordium.users.dto.report.ReportPostResponse;
import com.wordium.users.dto.report.UserReportResponse;
import com.wordium.users.dto.users.CountResponse;
import com.wordium.users.services.admin.AdminReportsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Admin - Reports Management", description = "Admin endpoints for viewing and managing post/user reports")
@RestController
@RequestMapping("/users/admin/reports")
public class AdminReportsController {

        private final AdminReportsService adminReportsService;

        public AdminReportsController(AdminReportsService adminReportsService) {
                this.adminReportsService = adminReportsService;
        }

        // ==================== POST REPORTS ====================

        @Operation(summary = "List all post reports", description = "Retrieve paginated post reports.")
        @GetMapping("/posts")
        public ResponseEntity<PaginatedResponse<ReportPostResponse>> getAllPostReports(
                        @Valid PaginationRequest paginationRequest) {
                Pageable pagebale = paginationRequest.toPageable();

                return ResponseEntity.ok(adminReportsService.getAllPostReports(pagebale));
        }

        @Operation(summary = "Get post report details", description = "Retrieve full details of a specific post report by ID.")
        @GetMapping("/posts/{id}")
        public ResponseEntity<ReportPostResponse> getPostReportById(@PathVariable Long id) {
                return ResponseEntity.ok(adminReportsService.getPostReportById(id));
        }

        @Operation(summary = "Resolve a post report", description = "Mark a post report as resolved.")
        @PatchMapping("/posts/{id}/resolve")
        public ResponseEntity<ReportPostResponse> resolvePostReport(@PathVariable Long id) {
                return ResponseEntity.ok(adminReportsService.resolvePostReport(id));
        }

        @Operation(summary = "Delete a post report", description = "Delete a post report permanently.")
        @DeleteMapping("/posts/{id}")
        public ResponseEntity<Void> deletePostReport(@PathVariable Long id) {
                adminReportsService.deletePostReport(id);
                return ResponseEntity.noContent().build();
        }

        // ==================== USER REPORTS ====================

        @Operation(summary = "List user reports", description = "Retrieve paginated user reports by status.")
        @GetMapping("/users")
        public ResponseEntity<PaginatedResponse<UserReportResponse>> getAllUserReports(
                        @Valid PaginationRequest paginationRequest, @RequestHeader("User-Id") Long id) {

                Pageable page = paginationRequest.toPageable();
                Page<UserReportResponse> reports = adminReportsService.getAllUserReports(id, page);
                return ResponseEntity.ok(PaginatedResponse.fromPage(reports));
        }

        @Operation(summary = "Get user report details", description = "Retrieve full details of a user report by ID.")
        @GetMapping("/users/{id}")
        public ResponseEntity<UserReportResponse> getUserReportById(@PathVariable Long id,
                        @RequestHeader("User-Id") Long idd) {
                return ResponseEntity.ok(adminReportsService.getUserReportById(id, idd));
        }

        @Operation(summary = "Resolve a user report", description = "Resolve a user report by an admin/moderator.")
        @PatchMapping("/users/{id}/resolve")
        public ResponseEntity<Void> resolveUserReport(
                        @PathVariable Long id,
                        @RequestHeader("User-Id") Long adminId) {

                adminReportsService.resolveUserReport(id, adminId);
                return ResponseEntity.ok().build();
        }

        @Operation(summary = "Delete a user report", description = "Delete a user report permanently.")
        @DeleteMapping("/users/{id}")
        public ResponseEntity<Void> deleteUserReport(@PathVariable Long id) {
                adminReportsService.deleteUserReport(id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/users/count")
        public ResponseEntity<CountResponse> getTotalUserReports() {
                return ResponseEntity.ok(
                                new CountResponse(adminReportsService.getTotalUserReports()));
        }

        @GetMapping("/posts/count")
        public ResponseEntity<CountResponse> getTotalPostsReports() {
                return ResponseEntity.ok(
                                new CountResponse(adminReportsService.getTotalUserReports()));
        }
}
