package com.wordium.posts.controllers;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.posts.dto.ReportPostResponse;
import com.wordium.posts.dto.ReportRequest;
import com.wordium.posts.services.ReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(
        name = "Reports",
        description = "Public endpoints for reporting posts"
)
@RestController
@RequestMapping("posts/{id}/reports")
public class ReportsPublicController {

    private final ReportService reportService;

    public ReportsPublicController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(
            summary = "Create a report",
            description = "Allows a  to report a post or another . Duplicate reports with the same reason are not allowed."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Report created successfully",
                content = @Content(schema = @Schema(implementation = ReportPostResponse.class))
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid request or duplicate report",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))
        )
    })
    @PostMapping
    public ResponseEntity<Void> createReport(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody ReportRequest request
    ) {
        reportService.createReport(userId,id, request);
        return ResponseEntity.ok().build();
    }
}
