// src/main/java/com/wordium/posts/models/Report.java
package com.wordium.posts.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "reports",
        uniqueConstraints = {
            @UniqueConstraint(
                    columnNames = {"reporter_id", "reported_post_id", "reported_user_id", "reason"},
                    name = "uk_reporter_target_reason"
            )
        })
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Reporter ID is required")
    @Column(name = "reporter_id", nullable = false)
    private Long reporterId;

    // Target: either post or user (at least one should be provided in business logic)
    @Column(name = "reported_post_id")
    private Long reportedPostId;

    @Column(name = "reported_user_id")
    private Long reportedUserId;

    @NotBlank(message = "Reason is required")
    @Size(max = 50, message = "Reason cannot exceed 500 characters")
    @Column(nullable = false, length = 500)
    private String reason;

    // @Size(max = 500, message = "Custom message cannot exceed 500 characters")
    // private String customMessage;
    // @NotBlank(message = "Status is required")
    // @Size(max = 20, message = "Status cannot exceed 20 characters")
    // @Column(nullable = false, length = 20)
    // private String status = "PENDING";  // e.g., "PENDING", "RESOLVED", "DISMISSED"
    @Column(name = "resolved_by")
    private Long resolvedById;

    private LocalDateTime resolvedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public Report() {
    }

    public Report(Long reporterId, Long reportedPostId, Long reportedUserId, String reason, String customMessage) {
        this.reporterId = reporterId;
        this.reportedPostId = reportedPostId;
        this.reportedUserId = reportedUserId;
        this.reason = reason;
        // this.customMessage = customMessage;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // @PreUpdate
    // protected void onUpdate() {
    //     if ("RESOLVED".equalsIgnoreCase(status) || "DISMISSED".equalsIgnoreCase(status)) {
    //         resolvedAt = LocalDateTime.now();
    //     }
    // }
    // Convenience methods
    public boolean isPostReport() {
        return reportedPostId != null;
    }

    public boolean isUserReport() {
        return reportedUserId != null;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public Long getReportedPostId() {
        return reportedPostId;
    }

    public void setReportedPostId(Long reportedPostId) {
        this.reportedPostId = reportedPostId;
    }

    public Long getReportedUserId() {
        return reportedUserId;
    }

    public void setReportedUserId(Long reportedUserId) {
        this.reportedUserId = reportedUserId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    // public String getCustomMessage() { return customMessage; }
    // public void setCustomMessage(String customMessage) { this.customMessage = customMessage; }
    // public String getStatus() { return status; }
    // public void setStatus(String status) { this.status = status; }
    public Long getResolvedById() {
        return resolvedById;
    }

    public void setResolvedById(Long resolvedById) {
        this.resolvedById = resolvedById;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
