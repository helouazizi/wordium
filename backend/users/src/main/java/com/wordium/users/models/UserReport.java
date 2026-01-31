package com.wordium.users.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_reports")
public class UserReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User being reported
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id", nullable = false)
    private Users reportedUser;

    // User who made the report
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by_user_id", nullable = false)
    private Users reportedBy;

    @Column(nullable = false)
    private String reason;

    private boolean resolved = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by_user_id")
    private Users resolvedBy;

    private String resolutionNote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(Users reportedUser) {
        this.reportedUser = reportedUser;
    }

    // Reported By
    public Users getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(Users reportedBy) {
        this.reportedBy = reportedBy;
    }

    // Reason
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    // Status
    public boolean getResolved() {
        return resolved;
    }

    public void setStatus(boolean status) {
        this.resolved = status;
    }

    // Created At
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Resolved At
    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    // Resolved By
    public Users getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(Users resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    // Resolution Note
    public String getResolutionNote() {
        return resolutionNote;
    }

    public void setResolutionNote(String resolutionNote) {
        this.resolutionNote = resolutionNote;
    }

}
