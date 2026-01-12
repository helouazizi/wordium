package com.wordium.posts.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinary.Cloudinary;
import com.wordium.posts.dto.CommentRequest;
import com.wordium.posts.dto.CommentResponse;
import com.wordium.posts.dto.PostReactionRequest;
import com.wordium.posts.dto.PostRequest;
import com.wordium.posts.dto.PostResponse;
import com.wordium.posts.dto.UserProfile;
import com.wordium.posts.exeptions.AccessDeniedException;
import com.wordium.posts.exeptions.BadRequestException;
import com.wordium.posts.exeptions.NotFoundException;
import com.wordium.posts.models.Comment;
import com.wordium.posts.models.Post;
import com.wordium.posts.models.PostReaction;
import com.wordium.posts.repo.CommentRepository;
import com.wordium.posts.repo.PostRepository;
import com.wordium.posts.repo.ReactionRepository;
import com.wordium.posts.services.PostService;
import com.wordium.posts.utils.UserEnrichmentHelper;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;
    private final UserEnrichmentHelper userEnrichmentHelper;
    private final Cloudinary cloudinary;

    public PostServiceImpl(PostRepository postRepository, UserEnrichmentHelper userEnrichmentHelper,
            ReactionRepository reactionRepository, CommentRepository commentRepository, Cloudinary cloudinary) {
        this.postRepository = postRepository;
        this.userEnrichmentHelper = userEnrichmentHelper;
        this.reactionRepository = reactionRepository;
        this.commentRepository = commentRepository;
        this.cloudinary = cloudinary;
    }

    @Override
    public PostResponse createPost(Long userId, PostRequest request) {

        Post post = new Post();
        post.setUserId(userId);
        post.setTitle(request.title());
        post.setContent(request.content());

        Post saved = postRepository.save(post);
        return mapToResponse(saved, new UserProfile(userId, null, null, "null", null, "null", null));
    }

    @Override
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        return userEnrichmentHelper.enrichSingle(
                post,
                Post::getUserId,
                this::mapToResponse);
    }

    @Override
    public Page<PostResponse> getFeed(Pageable pageable) {
        Page<Post> page = postRepository.findByFlaggedFalse(pageable);
        return userEnrichmentHelper.enrichPage(
                page,
                Post::getUserId,
                this::mapToResponse);
    }

    @Override
    public Page<PostResponse> getAllposts(Pageable pageable) {
        Page<Post> page = postRepository.findAll(pageable);
        return userEnrichmentHelper.enrichPage(
                page,
                Post::getUserId,
                this::mapToResponse);
    }

    @Override
    public Page<PostResponse> getPostsByUser(Long userId, Pageable pageable) {
        Page<Post> page = postRepository.findByUserId(userId, pageable);
        return userEnrichmentHelper.enrichPage(
                page,
                Post::getUserId,
                this::mapToResponse);
    }

    @Override
    public PostResponse updatePost(Long postId, Long userId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));

        if (!post.getUserId().equals(userId)) {
            throw new AccessDeniedException("You cannot update this post");
        }

        if (request.title() != null && !request.title().isBlank()) {
            post.setTitle(request.title());
        }

        if (request.content() != null && !request.content().isBlank()) {
            post.setContent(request.content());
        }

  

        Post updated = postRepository.save(post);
        return mapToResponse(updated, new UserProfile(userId, null, null, "null", null, "null", null));
    }

    @Override
    public void deletePost(Long postId, Long userId, String role) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));

        if (!post.getUserId().equals(userId) || !isAdmin(role)) {
            throw new AccessDeniedException("You cannot delete this post");
        }
        postRepository.delete(post);
    }

    @Override
    public void flagPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        post.setFlagged(true);
        postRepository.save(post);
    }

    @Override
    public void unflagPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        post.setFlagged(false);
        postRepository.save(post);
    }

    // likes
    @Override
    public void react(Long userId, Long postId, PostReactionRequest req) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post not found");
        }
        Optional<PostReaction> existing = reactionRepository.findByPostIdAndUserId(postId, userId);

        if (!"like".equals(req.reaction()) && !"unlike".equals(req.reaction())) {
            throw new BadRequestException("Bad Reaction");
        }

        if ("like".equals(req.reaction())) {

            if (existing.isPresent()) {
                return;
            }

            PostReaction reaction = new PostReaction();
            reaction.setPostId(postId);
            reaction.setUserId(userId);

            reactionRepository.save(reaction);
            postRepository.incrementLikesCount(postId);

            return;
        }

        if ("unlike".equals(req.reaction())) {
            if (existing.isEmpty()) {
                return;
            }

            reactionRepository.delete(existing.get());
            postRepository.decrementLikesCount(postId);
        }
    }

    private PostResponse mapToResponse(Post post, UserProfile userProfile) {
  

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                new UserProfile(null, null, null, userProfile.username(), null, userProfile.avatar(), null),
                post.getLikesCount(),
                post.getCommentsCount(),
                post.getRportCount(),
                post.isReported(),
                post.isFlagged(),
                post.getCreatedAt(),
                post.getUpdatedAt());
    }

    private CommentResponse mapCommentToResponse(Comment comment, UserProfile userProfile) {
        return new CommentResponse(
                comment.getId(),
                comment.getPostId(),
                comment.getContent(),
                comment.getCreatedAt(),
                userProfile);
    }

    private boolean isAdmin(String role) {
        return role != null && role.equalsIgnoreCase("ADMIN");
    }

    // comments
    @Override
    @Transactional
    public CommentResponse createComment(Long userId, Long postId, CommentRequest request) {

        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post not found");
        }
        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(request.content());

        Comment saved = commentRepository.save(comment);
        postRepository.incrementCommentsCount(postId);

        return userEnrichmentHelper.enrichSingle(
                saved,
                Comment::getUserId,
                this::mapCommentToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getPostComments(Long postId, Pageable pageable) {
        if (!postRepository.existsById(postId)) {
            throw new NotFoundException("Post not found");
        }
        Page<Comment> page = commentRepository.findByPostIdOrderByCreatedAtAsc(postId, pageable);

        return userEnrichmentHelper.enrichPage(
                page,
                Comment::getUserId,
                this::mapCommentToResponse);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long postId, Long commentId) {

        Comment comment = commentRepository
                .findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        if (!comment.getUserId().equals(userId)) {
            throw new AccessDeniedException("You cannot delete this comment");
        }

        commentRepository.delete(comment);
        postRepository.decrementCommentsCount(postId);
    }

    @Override
    public Map<String, Object> getSignature() {

        long timestamp = System.currentTimeMillis() / 1000L;

        Map<String, Object> params = new HashMap<>();
        params.put("timestamp", timestamp);
        params.put("folder", "posts");
        params.put("upload_preset", "ml_default"); // must match Angular

        // Generate the signature
        String signature = cloudinary.apiSignRequest(params, cloudinary.config.apiSecret);

        // Send to Angular
        Map<String, Object> response = new HashMap<>();
        response.put("signature", signature);
        response.put("timestamp", timestamp);
        response.put("cloudName", cloudinary.config.cloudName);
        response.put("apiKey", cloudinary.config.apiKey);
        response.put("folder", "posts");
        response.put("upload_preset", "ml_default");

        return response;
    }

}
