package com.wordium.posts.services.impl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.wordium.posts.services.CloudinaryService;
import com.wordium.posts.services.PostService;
import com.wordium.posts.utils.UserEnrichmentHelper;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;
    private final UserEnrichmentHelper userEnrichmentHelper;
    private final CloudinaryService cloudinaryService;

    public PostServiceImpl(PostRepository postRepository, UserEnrichmentHelper userEnrichmentHelper,
            ReactionRepository reactionRepository, CommentRepository commentRepository,
            CloudinaryService cloudinaryService) {
        this.postRepository = postRepository;
        this.userEnrichmentHelper = userEnrichmentHelper;
        this.reactionRepository = reactionRepository;
        this.commentRepository = commentRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public PostResponse createPost(Long userId, PostRequest request) {

        Post post = new Post();
        post.setUserId(userId);
        post.setTitle(request.title());
        post.setContent(request.content());

        Post saved = postRepository.save(post);

        if (request.media() != null) {
            for (var media : request.media()) {
                this.cloudinaryService.finalizeUpload(media);
            }
        }

        return mapToResponse(saved, new UserProfile(userId, null, null, "null", null, "null", null, null, null, null,
                null, null, null, null, null, null, null, null), false);
    }

    @Override
    public PostResponse getPostById(Long userId, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        boolean isLiked = userId != null && reactionRepository.findByPostIdAndUserId(post.getId(), userId).isPresent();

        return userEnrichmentHelper.enrichSingle(
                post,
                Post::getUserId,
                (p, userProfile) -> mapToResponse(p, userProfile, isLiked));
    }

    @Override
    public Page<PostResponse> getFeed(Pageable pageable, Long userId) {
        Page<Post> page = postRepository.findByFlaggedFalse(pageable);
        return enrichWithLikes(page, userId);
    }

    @Override
    public Page<PostResponse> getAllposts(Long userId, Pageable pageable) {
        Page<Post> page = postRepository.findAll(pageable);
        return enrichWithLikes(page, userId);
    }

    @Override
    public Page<PostResponse> getPostsByUser(Long targetUserId, Pageable pageable) {
        Page<Post> page = postRepository.findByUserId(targetUserId, pageable);
        return enrichWithLikes(page, targetUserId);
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
        return mapToResponse(updated, new UserProfile(userId, null, null, "null", null, "null", null, null, null, null,
                null, null, null, null, null, null, null, null), false);
    }

    @Override
    public void deletePost(Long userId, Long postId, String role) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));

        if (!post.getUserId().equals(userId) && !isAdmin(role)) {
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

    private PostResponse mapToResponse(Post post, UserProfile userProfile, boolean isliked) {

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                new UserProfile(userProfile.id(), userProfile.username(), null, "null", null, userProfile.avatar(),
                        null, null, null, null, null, null, null, null, null, null, null, null),
                post.getLikesCount(),
                post.getCommentsCount(),
                post.getRportCount(),
                isliked,
                post.isReported(),
                post.isFlagged(),
                post.getCreatedAt(),
                post.getUpdatedAt());
    }

    private Page<PostResponse> enrichWithLikes(Page<Post> page, Long currentUserId) {
        if (page.isEmpty()) {
            return Page.empty();
        }

        Set<Long> postIds = page.getContent().stream().map(Post::getId).collect(Collectors.toSet());

        Set<Long> likedPostIds = (currentUserId != null)
                ? reactionRepository.findLikedPostIdsByUserIdAndPostIdIn(currentUserId, postIds)
                : Set.of();

        return userEnrichmentHelper.enrichPage(
                page,
                Post::getUserId,
                (post, userProfile) -> {
                    boolean isLiked = likedPostIds.contains(post.getId());
                    return mapToResponse(post, userProfile, isLiked);
                });
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
        Page<Comment> page = commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);

        return userEnrichmentHelper.enrichPage(
                page,
                Comment::getUserId,
                this::mapCommentToResponse);
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, String role, Long postId, Long commentId) {

        Comment comment = commentRepository
                .findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        if (!comment.getUserId().equals(userId) && !isAdmin(role)) {
            throw new AccessDeniedException("You cannot delete this comment");
        }

        commentRepository.delete(comment);
        postRepository.decrementCommentsCount(postId);
    }

}
