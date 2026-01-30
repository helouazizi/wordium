package com.wordium.users.services.admin.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wordium.users.client.PostServiceClient;
import com.wordium.users.dto.PaginatedResponse;
import com.wordium.users.dto.Stats;
import com.wordium.users.dto.report.ReportPostResponse;
import com.wordium.users.dto.report.ReportStatus;
import com.wordium.users.dto.report.UserReportResponse;
import com.wordium.users.dto.users.UserProfile;
import com.wordium.users.exceptions.NotFoundException;
import com.wordium.users.models.UserReport;
import com.wordium.users.models.Users;
import com.wordium.users.repo.FollowersRepo;
import com.wordium.users.repo.UserReportRepo;
import com.wordium.users.repo.UsersRepo;
import com.wordium.users.services.admin.AdminReportsService;

@Service
public class AdminReportsServiceImpl implements AdminReportsService {

    private final PostServiceClient postServiceClient;
    private final UserReportRepo userReportRepo;
    private final UsersRepo usersRepo;

    private final FollowersRepo followersRepo;

    public AdminReportsServiceImpl(PostServiceClient postServiceClient, UserReportRepo userReportRepo,
            UsersRepo usersRepo, FollowersRepo followersRepo) {
        this.postServiceClient = postServiceClient;
        this.userReportRepo = userReportRepo;
        this.usersRepo = usersRepo;
        this.followersRepo = followersRepo;
    }

    @Override
    public PaginatedResponse<ReportPostResponse> getAllPostReports(Pageable page) {
        return postServiceClient.getAllReports(page);
    }

    @Override
    public ReportPostResponse getPostReportById(Long id) {
        return postServiceClient.getReportById(id);
    }

    @Override
    public ReportPostResponse resolvePostReport(Long id) {
        return postServiceClient.resolveReport(id);
    }

    @Override
    public void deletePostReport(Long id) {
        postServiceClient.deleteReport(id);

    }

    @Override
    public Page<UserReportResponse> getAllUserReports(Long viewerId, Pageable pageable) {
        return userReportRepo.findAll(pageable)
                .map(r -> toResponse(r, viewerId));
    }

    @Override
    public UserReportResponse getUserReportById(Long id, Long viewerId) {
        UserReport report = userReportRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("User report not found"));

        // Pass viewerId to resolve full UserProfile
        return toResponse(report, viewerId);
    }

    @Override
    public void resolveUserReport(Long reportId, Long adminId) {
        UserReport report = userReportRepo.findById(reportId)
                .orElseThrow(() -> new NotFoundException("User report not found"));

        Users admin = usersRepo.findById(adminId)
                .orElseThrow(() -> new NotFoundException("Admin user not found"));

        report.setStatus(ReportStatus.RESOLVED);
        report.setResolvedAt(LocalDateTime.now());
        report.setResolvedBy(admin);

        userReportRepo.save(report);
    }

    @Override
    public void deleteUserReport(Long reportId) {
        userReportRepo.deleteById(reportId);
    }

    @Override
    public Long getTotalUserReports() {
        return userReportRepo.count();
    }

    @Override
    public Long getTotalPostsReports() {
        return postServiceClient.postsReportsCount();
    }

    private UserReportResponse toResponse(UserReport r, Long viewerId) {
        UserProfile reportedByProfile = toUserProfile(r.getReportedBy(), viewerId);

        return new UserReportResponse(
                r.getId(),
                r.getReportedUser().getId(),
                reportedByProfile,
                r.getReason(),
                r.getStatus().name(),
                "user",
                r.getCreatedAt(),
                r.getResolvedAt());
    }

    private UserProfile toUserProfile(Users u, Long viewerId) {

        Boolean isFollowing = viewerId != null &&
                followersRepo.existsByFollowerIdAndFollowedId(viewerId, u.getId());

        Boolean followsMe = viewerId != null &&
                followersRepo.existsByFollowerIdAndFollowedId(u.getId(), viewerId);

        Stats stats = new Stats(
                followersRepo.countByFollowedId(u.getId()),
                followersRepo.countByFollowerId(u.getId()), null, null);

        return new UserProfile(
                u.getId(),
                u.getUsername(),
                u.getDisplayName(),
                u.getEmail(),
                u.getRole(),
                u.getAvatar(),
                u.getCover(),
                u.getBio(),
                u.getLocation(),
                u.getCreatedAt(),
                u.getUpdatedAt(),
                u.getLastLoginAt(),
                u.isVerified(),
                u.isBanned(),
                isFollowing,
                followsMe,
                stats,
                u.getSocial());
    }

}