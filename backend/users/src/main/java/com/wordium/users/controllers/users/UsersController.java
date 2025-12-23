package com.wordium.users.controllers.users;

import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.users.dto.BatchUsersRequest;
import com.wordium.users.dto.SignUpRequest;
import com.wordium.users.dto.SignUpResponse;
import com.wordium.users.dto.UpdateProfileRequest;
import com.wordium.users.dto.UsersResponse;
import com.wordium.users.services.users.UsersService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
@Tag(name = "Users - Management", description = "Users endpoints")
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private final UsersService userService;

    public UsersController(UsersService userService) {
        this.userService = userService;
    }

    @Hidden
    @PostMapping("/internal/create")
    public ResponseEntity<SignUpResponse> createUser(@Valid @RequestBody SignUpRequest req) {
        SignUpResponse createdUser = userService.createUser(req);
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(createdUser);
    }

    @Hidden
    @GetMapping("/internal/lookup")
    public ResponseEntity<SignUpResponse> lookupUser(@RequestParam(required = false) String email,
            @RequestParam(required = false) String username) {
        SignUpResponse user = userService.findByEmailOrUsername(email, username);
        return ResponseEntity.ok(user);
    }

    @Hidden
    @PostMapping("/internal/batch")
    public ResponseEntity<List<UsersResponse>> getUsersProfiles(@RequestBody BatchUsersRequest req) {
        List<UsersResponse> users = userService.getUsers(req);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    @Operation(summary = "Get session profile", description = "Fetch a session profile using id from header ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User fetched successfully", content = @Content(schema = @Schema(implementation = UsersResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))), })

    public ResponseEntity<UsersResponse> getSessionProfile(@RequestHeader("User-Id") Long userId) {
        UsersResponse user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/me")
    @Operation(summary = "Update User profile", description = "Update User profile using id from header ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User profile updated successfully", content = @Content(schema = @Schema(implementation = UsersResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))), })
    public ResponseEntity<UsersResponse> upadteProfile(@RequestHeader("User-Id") Long userId,
            @Valid @RequestBody UpdateProfileRequest req) {
        UsersResponse user = userService.updateUserProfile(userId, req);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get User profile for all kind of users ", description = "Fetch a user profile using id as param")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User fetched successfully", content = @Content(schema = @Schema(implementation = UsersResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))), })

    public ResponseEntity<UsersResponse> getUserProfile(@PathVariable Long userId) {
        UsersResponse user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

}
