package com.wordium.users.controller;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.users.dto.SignUpRequest;
import com.wordium.users.dto.SignUpResponse;
import com.wordium.users.dto.UsersResponse;
import com.wordium.users.service.UsersService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private final UsersService userService;

    public UsersController(UsersService userService) {
        this.userService = userService;
    }

    @Hidden
    @PostMapping("/create")
    public ResponseEntity<SignUpResponse> createUser(@Valid @RequestBody SignUpRequest req) {
        SignUpResponse createdUser = userService.createUser(req);
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(createdUser);
    }

    @Hidden
    @GetMapping("/lookup")
    public ResponseEntity<SignUpResponse> lookupUser(@RequestParam(required = false) String email,
            @RequestParam(required = false) String username) {
        SignUpResponse user = userService.findByEmailOrUsername(email, username);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Get session profile", description = "Fetch a session profile using id from header ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User fetched successfully", content = @Content(schema = @Schema(implementation = UsersResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    @GetMapping("/me")
    public ResponseEntity<UsersResponse> getUserProfile(@RequestHeader("User-Id") Long userId) {
        UsersResponse user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

}
