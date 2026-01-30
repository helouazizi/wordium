package com.wordium.posts.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.wordium.posts.dto.PostsSegnatureResponse;
import com.wordium.posts.services.CloudinaryService;
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
        private final CloudinaryService cloudinaryService;

        public PublicPostController(PostServiceImpl postService, CloudinaryService cloudinaryService) {
                this.postService = postService;
                this.cloudinaryService = cloudinaryService;
        }

        @Operation(summary = "Create a new post")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Post created successfully", content = @Content(schema = @Schema(implementation = PostResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PostMapping()
        public ResponseEntity<PostResponse> createPost(
                        @RequestHeader("User-Id") Long userId,
                        @RequestBody @Valid PostRequest request) {
                PostResponse post = postService.createPost(userId, request);
                return ResponseEntity.status(201).body(post);
        }

        @PatchMapping()
        public ResponseEntity<PostResponse> updatePost(
                        @RequestHeader("User-Id") Long userId,
                        @RequestBody @Valid PostRequest request) {
                PostResponse post = postService.updatePost(userId, request);
                return ResponseEntity.status(201).body(post);
        }

        @Operation(summary = "Get paginated posts feed", description = "Returns a paginated list of posts. Supports page, size, sortBy, and sortDir query parameters.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Posts fetched successfully", content = @Content(schema = @Schema(implementation = PaginatedResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @GetMapping
        public ResponseEntity<PaginatedResponse<PostResponse>> getFeed(@Valid PaginationRequest paginationRequest,
                        @RequestHeader("User-Id") Long userId) {
                Pageable pageable = paginationRequest.toPageable();
                var page = postService.getFeed(pageable, userId);
                return ResponseEntity.ok(PaginatedResponse.fromPage(page));
        }

        @GetMapping("/user/{userId}")
        public ResponseEntity<PaginatedResponse<PostResponse>> getPostsByUser(@PathVariable Long userId,
                        @Valid PaginationRequest paginationRequest) {
                Pageable pageable = paginationRequest.toPageable();
                var page = postService.getPostsByUser(userId, pageable);
                return ResponseEntity.ok(PaginatedResponse.fromPage(page));
        }

        @Operation(summary = "Get post by ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Post fetched successfully", content = @Content(schema = @Schema(implementation = PostResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @GetMapping("/{id}")
        public ResponseEntity<PostResponse> getPost(@PathVariable Long id, @RequestHeader("User-Id") Long userId) {
                return ResponseEntity.ok(postService.getPostById(userId, id));
        }

        @Operation(summary = "React to a post (like/unlike)")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Reaction processed successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid reaction", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PostMapping("/{id}/react")
        public ResponseEntity<Void> react(
                        @RequestHeader("User-Id") Long userId,
                        @PathVariable Long id,
                        @Valid @RequestBody PostReactionRequest req) {
                postService.react(userId, id, req);
                return ResponseEntity.ok().build();
        }

        @Operation(summary = "Create a comment on a post")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Comment created successfully", content = @Content(schema = @Schema(implementation = CommentResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @PostMapping("/{id}/comments")
        public ResponseEntity<CommentResponse> createComment(
                        @RequestHeader("User-Id") Long userId,
                        @PathVariable Long id,
                        @Valid @RequestBody CommentRequest request) {

                CommentResponse comment = postService.createComment(userId, id, request);
                return ResponseEntity.status(201).body(comment);
        }

        @Operation(summary = "Get all comments for a post (paginated)")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Comments fetched successfully", content = @Content(schema = @Schema(implementation = PaginatedResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
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
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "403", description = "Forbidden - not owner of comment", content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                        @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
        })
        @DeleteMapping("/{postId}/comments/{commentId}")
        public ResponseEntity<Void> deleteComment(
                        @RequestHeader("User-Id") Long userId,
                        @RequestHeader("User-Role") String role,
                        @PathVariable Long postId,
                        @PathVariable Long commentId) {

                postService.deleteComment(userId, role, postId, commentId);
                return ResponseEntity.noContent().build();
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePost(
                        @RequestHeader("User-Id") Long userId, @RequestHeader("User-Role") String role,
                        @PathVariable Long id) {
                postService.deletePost(userId, id, role);
                return ResponseEntity.noContent().build();
        }

        @GetMapping("/signature")
        public ResponseEntity<PostsSegnatureResponse> getUploadSignature() {
                var res = cloudinaryService.getSignature();
                return ResponseEntity.ok(new PostsSegnatureResponse(res));
        }

}
