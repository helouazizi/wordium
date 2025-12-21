package com.wordium.users.controllers.followers;

import java.util.List;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.users.dto.FollowResponse;
import com.wordium.users.dto.UsersResponse;
import com.wordium.users.services.followers.FollowersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/users")
public class FollowersController {

    private final FollowersService followersService;

    public FollowersController(FollowersService followersService) {
        this.followersService = followersService;
    }

    @Operation(summary = "Get a Session followers", description = "Get a Session followers using id from request header")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "followers fetched successfully", content = @Content(schema = @Schema(implementation = FollowResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    @GetMapping("/me/followers")
    public ResponseEntity<FollowResponse> sessionFollowers(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long targetUserId) {
        followersService.followUser(userId, targetUserId);
        return ResponseEntity.ok(new FollowResponse("followers fetched successfully"));
    }

    @Operation(summary = "Follow a User", description = "Follow a user profile using targetUserId as PathVariable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User Followed successfully", content = @Content(schema = @Schema(implementation = FollowResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    @PostMapping("/{targetUserId}/follow")
    public ResponseEntity<FollowResponse> follow(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long targetUserId) {
        followersService.followUser(userId, targetUserId);
        return ResponseEntity.ok(new FollowResponse("User Followed successfully"));
    }

    @Operation(summary = "Unfollow a User", description = "Unfollow a user profile using targetUserId as PathVariable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User Unfollowed successfully", content = @Content(schema = @Schema(implementation = FollowResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    @DeleteMapping("/{targetUserId}/unfollow")
    public ResponseEntity<FollowResponse> unfollow(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long targetUserId) {
        followersService.unfollowUser(userId, targetUserId);
        return ResponseEntity.ok(new FollowResponse("User Unfollowed successfully"));
    }

    @Operation(summary = "Get a User followers", description = "Get a User followers as list  using userId as PathVariable")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User followers fetched successfully", content = @Content(schema = @Schema(implementation = UsersResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UsersResponse>> followers(
            @PathVariable Long userId) {
        return ResponseEntity.ok(followersService.getFollowers(userId));
    }
}
