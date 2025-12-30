package com.wordium.users.controllers.admin;

import java.util.List;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.users.dto.PostResponse;
import com.wordium.users.services.admin.AdminPostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin - Posts Management", description = "Admin endpoints for managing user posts")
@RestController
@RequestMapping("/users/admin/posts")
public class AdminPostsController {

    private final AdminPostService adminPostService;

    public AdminPostsController(AdminPostService adminPostService) {
        this.adminPostService = adminPostService;
    }

    @Operation(
            summary = "List all posts",
            description = "Retrieve a list of all posts in the system. Accessible only to admins."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Posts fetched successfully",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - User is not an admin",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = adminPostService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @Operation(
            summary = "Get post by ID",
            description = "Retrieve detailed information about a specific post using its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post fetched successfully",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not an admin",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        PostResponse post = adminPostService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @Operation(
            summary = "Delete a post",
            description = "Permanently delete a post by its ID. Only admins can perform this action."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not an admin",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        adminPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Flag a post",
            description = "Mark a post as flagged (e.g., for review, hiding, or warning). Toggles or sets flagged = true."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post flagged successfully",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not an admin",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping("/{id}/flag")
    public ResponseEntity<PostResponse> flagPost(@PathVariable Long id) {
        PostResponse post = adminPostService.flagPost(id);
        return ResponseEntity.ok(post);
    }

    @Operation(
            summary = "Unflag a post",
            description = "Remove the flagged status from a post."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post unflagged successfully",
                    content = @Content(schema = @Schema(implementation = PostResponse.class))),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden - Not an admin",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping("/{id}/unflag")
    public ResponseEntity<PostResponse> unflagPost(@PathVariable Long id) {
        PostResponse post = adminPostService.unflagPost(id);
        return ResponseEntity.ok(post);
    }
}