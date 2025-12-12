package com.wordium.users.controller;

import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.users.dto.FailResponse;
import com.wordium.users.dto.SignUpRequest;
import com.wordium.users.dto.SuccessResponse;
import com.wordium.users.dto.UsersResponse;
import com.wordium.users.service.FollowersService;
import com.wordium.users.service.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UsersService userService;
    private final FollowersService followersService;

    public UserController(UsersService userService, FollowersService followersService) {
        this.userService = userService;
        this.followersService = followersService;
    }

    @Operation(summary = "Create a new user", description = "Creates a new user. Internal service call requires a token.(internal use only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(schema = @Schema(implementation = FailResponse.class))),

    })
    @PostMapping("/create")
    public ResponseEntity<SuccessResponse<UsersResponse>> createUser(@Valid @RequestBody SignUpRequest req) {
        UsersResponse createdUser = userService.createUser(req);
        return ResponseEntity.status(HttpStatus.SC_CREATED)
                .body(new SuccessResponse<>("User created successfully", createdUser));
    }

    @Operation(summary = "Get user by email", description = "Fetch a user by email. Internal service call requires a token.(internal use only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User fetched successfully", content = @Content(schema = @Schema(implementation = UsersResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = FailResponse.class))),
    })
    @PostMapping("/by-email")
    public ResponseEntity<SuccessResponse<UsersResponse>> getUserByEmail(@Valid @RequestBody SignUpRequest req) {
        UsersResponse user = userService.getUserByEmail(req.email());
        return ResponseEntity.ok(new SuccessResponse<>("User created successfully", user));
    }

    @Operation(summary = "Get Session User profile", description = "Fetch a user loged profile using id from request headers ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile fetched successfully", content = @Content(schema = @Schema(implementation = UsersResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = FailResponse.class))),
    })
    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<UsersResponse>> getSesstionProfile(@RequestHeader("User-Id") Long userId) {
        UsersResponse user = userService.getUserProfile(userId);
        return ResponseEntity.ok(new SuccessResponse<>("Profile fetched successfully", user));
    }

    @Operation(summary = "Get User profile", description = "Fetch a existing user profile using id ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile fetched successfully", content = @Content(schema = @Schema(implementation = UsersResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = FailResponse.class))),
    })

    @GetMapping("/{userId}")
    public ResponseEntity<SuccessResponse<UsersResponse>> getUserProfile(@PathVariable Long userId) {
        UsersResponse user = userService.getUserProfile(userId);
        return ResponseEntity.ok(new SuccessResponse<>("Profile fetched successfully", user));
    }

    @Operation(summary = "Update user profile", description = "Updates the authenticated user's profile details (bio, location, display name, avatar).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully", content = @Content(schema = @Schema(implementation = UsersResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = FailResponse.class)))
    })
    @PostMapping("/me")
    public ResponseEntity<SuccessResponse<UsersResponse>> updateUserProfile(
            @RequestHeader("User-Id") Long userId,
            @Valid @RequestBody SignUpRequest req) {
        UsersResponse updatedUser = userService.updateUserProfile(userId, req);
        return ResponseEntity.ok(new SuccessResponse<>("Profile updated successfully", updatedUser));
    }

    @Operation(summary = "Follow a user", description = "Allows the authenticated user to follow another user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully followed"),
            @ApiResponse(responseCode = "400", description = "Invalid follow request", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = FailResponse.class)))
    })
    @PostMapping("/{targetUserId}/follow")
    public ResponseEntity<SuccessResponse<?>> followUser(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long targetUserId) {
        followersService.followUser(userId, targetUserId);
        return ResponseEntity.ok(new SuccessResponse<>("User Successfully followed", null));
    }

    @Operation(summary = "Unfollow a user", description = "Allows the authenticated user to unfollow another user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully unfollowed"),
            @ApiResponse(responseCode = "400", description = "Invalid unfollow request", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = FailResponse.class)))
    })
    @DeleteMapping("/{targetUserId}/unfollow")
    public ResponseEntity<SuccessResponse<?>> unfollowUser(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long targetUserId) {
        followersService.unfollowUser(userId, targetUserId);
        return ResponseEntity.ok(new SuccessResponse<>("User Successfully followed", null));
    }

    @Operation(summary = "Get user followers", description = "Returns a list of users who follow the specified user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Followers fetched successfully", content = @Content(schema = @Schema(implementation = UsersResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = FailResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = FailResponse.class)))
    })
    @GetMapping("/{userId}/followers")
    public ResponseEntity<SuccessResponse<List<UsersResponse>>> getFollowers(
            @PathVariable Long userId) {
        List<UsersResponse> folowers = followersService.getFollowers(userId);
        return ResponseEntity.ok(new SuccessResponse<>("Followers fetched successfully", folowers));
    }

}
