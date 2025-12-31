package com.wordium.posts.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.wordium.posts.dto.CommentRequest;
import com.wordium.posts.dto.CommentResponse;
import com.wordium.posts.dto.PostImageResponse;
import com.wordium.posts.dto.PostReactionRequest;
import com.wordium.posts.dto.PostRequest;
import com.wordium.posts.dto.PostResponse;
import com.wordium.posts.dto.UserProfile;
import com.wordium.posts.exeptions.AccessDeniedException;
import com.wordium.posts.exeptions.BadRequestException;
import com.wordium.posts.exeptions.NotFoundException;
import com.wordium.posts.models.Comment;
import com.wordium.posts.models.Post;
import com.wordium.posts.models.PostImage;
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
        if (request.files() != null) {
            for (MultipartFile file : request.files()) {
                if (file.isEmpty()) {
                    throw new BadRequestException("One of the uploaded files is empty");
                }

                if (!isValidContentType(file.getContentType())) {
                    throw new BadRequestException(
                            "Unsupported file type. Only images and videos are allowed");
                }

                if (file.getSize() > 10_000_000) {
                    throw new BadRequestException(
                            "File size exceeds the maximum allowed limit (10MB)");
                }

            }
        }

        List<String> fileUrls = uploadFiles(request.files());

        Post post = new Post();
        post.setUserId(userId);
        post.setTitle(request.title());
        post.setContent(request.content());

        if (fileUrls != null && !fileUrls.isEmpty()) {
            for (int i = 0; i < fileUrls.size(); i++) {
                String url = fileUrls.get(i);
                PostImage image = new PostImage();
                image.setUrl(url);
                image.setDisplayOrder(i);
                post.addImage(image);
            }
        }

        Post saved = postRepository.save(post);
        return mapToResponse(saved, new UserProfile(userId, null, null, "null", null, "null", null));
    }

    private boolean isValidContentType(String contentType) {
        return contentType != null && (contentType.startsWith("image/") || contentType.startsWith("video/"));
    }

    public List<String> uploadFiles(MultipartFile[] files) {
        List<String> urls = new ArrayList<>();
        if (files == null)
            return urls;

        for (MultipartFile file : files) {
            try {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                        ObjectUtils.asMap(
                                "resource_type", "auto",  "folder", "posts"));
                urls.add(uploadResult.get("secure_url").toString());
            } catch (IOException e) {
                throw new BadRequestException(
                        "Failed to upload media file. Please try again later");
            }

        }

        return urls;
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

        if (request.files() != null && request.files().length > 0) {
            List<String> uploadedUrls = uploadFiles(request.files());

            // clear old images
            post.getImages().clear();

            for (int i = 0; i < uploadedUrls.size(); i++) {
                PostImage image = new PostImage();
                image.setUrl(uploadedUrls.get(i));
                image.setDisplayOrder(i);
                post.addImage(image);
            }
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
        List<PostImageResponse> images = post.getImages().stream()
                .map(img -> new PostImageResponse(
                        img.getId(),
                        img.getUrl(),
                        img.getAltText(),
                        img.getDisplayOrder()))
                .toList();

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                new UserProfile(null, null, null, userProfile.username(), null, userProfile.avatar(), null),
                images,
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

}
