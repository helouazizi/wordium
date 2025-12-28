package com.wordium.posts.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wordium.posts.dto.CommentRequest;
import com.wordium.posts.dto.CommentResponse;
import com.wordium.posts.dto.PaginatedResponse;
import com.wordium.posts.dto.PaginationRequest;
import com.wordium.posts.dto.PostReactionRequest;
import com.wordium.posts.dto.PostRequest;
import com.wordium.posts.dto.PostResponse;
import com.wordium.posts.services.impl.PostServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Posts", description = "Public endpoints for posts, reactions, and comments")
@RestController
@RequestMapping("/posts")
public class PublicPostController {

    private final PostServiceImpl postService;

    public PublicPostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @Operation(summary = "Create a new post")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Post created successfully",
                content = @Content(schema = @Schema(implementation = PostResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestHeader("User-Id") Long userId,
            @Valid @RequestBody PostRequest request) {
        PostResponse post = postService.createPost(userId, request);
        return ResponseEntity.status(201).body(post);
    }

    @Operation(summary = "Get paginated posts feed",
            description = "Returns a paginated list of posts. Supports page, size, sortBy, and sortDir query parameters.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Posts fetched successfully",
                content = @Content(schema = @Schema(implementation = PaginatedResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    public ResponseEntity<PaginatedResponse<PostResponse>> getFeed(@Valid PaginationRequest paginationRequest) {
        Pageable pageable = paginationRequest.toPageable();
        var page = postService.getFeed(pageable);
        return ResponseEntity.ok(PaginatedResponse.fromPage(page));
    }

    @Operation(summary = "Get post by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Post fetched successfully",
                content = @Content(schema = @Schema(implementation = PostResponse.class))),
        @ApiResponse(responseCode = "404", description = "Post not found",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Operation(summary = "React to a post (like/unlike)")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Reaction processed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid reaction",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Post not found",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/{id}/react")
    public ResponseEntity<Void> react(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody PostReactionRequest req) {
        postService.react(userId, id, req);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Create a comment on a post")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Comment created successfully",
                content = @Content(schema = @Schema(implementation = CommentResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Post not found",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request) {

        request = new CommentRequest(id, request.commentId(), request.content());
        CommentResponse comment = postService.createComment(userId, id, request);
        return ResponseEntity.status(201).body(comment);
    }

    @Operation(summary = "Get all comments for a post (paginated)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Comments fetched successfully",
                content = @Content(schema = @Schema(implementation = PaginatedResponse.class))),
        @ApiResponse(responseCode = "404", description = "Post not found",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<PaginatedResponse<CommentResponse>> getComments(
            @PathVariable Long id,
            @Valid PaginationRequest paginationRequest) {

        Pageable pageable = paginationRequest.toPageable();
        var page = postService.getPostComments(id, pageable);
        return ResponseEntity.ok(PaginatedResponse.fromPage(page));
    }

    @Operation(summary = "Delete a comment")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - not owner of comment",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "404", description = "Comment not found",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}/comments")
    public ResponseEntity<Void> deleteComment(
            @RequestHeader("User-Id") Long userId,
            @Valid @RequestBody CommentRequest req) {

        postService.deleteComment(userId, req);
        return ResponseEntity.noContent().build();
    }
}
