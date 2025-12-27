package com.wordium.posts.services.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wordium.posts.dto.PostImageResponse;
import com.wordium.posts.dto.PostRequest;
import com.wordium.posts.dto.PostResponse;
import com.wordium.posts.dto.UserProfile;
import com.wordium.posts.models.Post;
import com.wordium.posts.models.PostImage;
import com.wordium.posts.repo.PostRepository;
import com.wordium.posts.services.PostService;
import com.wordium.posts.utils.UserEnrichmentHelper;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserEnrichmentHelper userEnrichmentHelper;

    public PostServiceImpl(PostRepository postRepository, UserEnrichmentHelper userEnrichmentHelper) {
        this.postRepository = postRepository;
        this.userEnrichmentHelper = userEnrichmentHelper;
    }

    @Override
    public PostResponse createPost(Long userId, PostRequest request) {
        Post post = new Post();
        post.setUserId(userId);
        post.setTitle(request.title());
        post.setContent(request.content());

        if (request.images() != null && !request.images().isEmpty()) {
            request.images().forEach(imgReq -> {
                PostImage image = new PostImage();
                image.setUrl(imgReq.url());
                image.setAltText(imgReq.altText());
                image.setDisplayOrder(imgReq.displayOrder() != null ? imgReq.displayOrder() : 0);
                post.addImage(image);
            });
        }

        Post saved = postRepository.save(post);
        return mapToResponse(saved, new UserProfile(userId, null, null, "null", null, "null", null));
    }

    @Override
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + id));
        return userEnrichmentHelper.enrichSingle(
                post,
                Post::getUserId,
                this::mapToResponse
        );
    }

    @Override
    public Page<PostResponse> getFeed(Pageable pageable) {
        Page<Post> page = postRepository.findByFlaggedFalse(pageable);
        return userEnrichmentHelper.enrichPage(
                page,
                Post::getUserId,
                this::mapToResponse
        );
    }

    @Override
    public Page<PostResponse> getPostsByUser(Long userId, Pageable pageable) {
        Page<Post> page = postRepository.findByUserId(userId, pageable);
        return userEnrichmentHelper.enrichPage(
                page,
                Post::getUserId,
                this::mapToResponse
        );
    }

    @Override
    public PostResponse updatePost(Long postId, Long userId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));

        // Optional: Add ownership check here
        // if (!post.getUserId().equals(userId)) throw new AccessDeniedException("...");
        if (request.content() != null && !request.content().isBlank()) {
            post.setContent(request.content());
        }

        // Handle image updates (simple: clear and add new)
        if (request.images() != null) {
            post.getImages().clear();  // remove old images
            request.images().forEach(imgReq -> {
                PostImage image = new PostImage();
                image.setUrl(imgReq.url());
                image.setAltText(imgReq.altText());
                image.setDisplayOrder(imgReq.displayOrder() != null ? imgReq.displayOrder() : 0);
                post.addImage(image);
            });
        }

        Post updated = postRepository.save(post);
        return mapToResponse(updated, new UserProfile(userId, null, null, "null", null, "null", null));
    }

    @Override
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));

        // Optional: ownership/admin check
        // if (!post.getUserId().equals(userId) && !isAdmin(userId)) throw new AccessDeniedException(...);
        postRepository.delete(post);
    }

    @Override
    public void flagPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
        post.setFlagged(true);
        postRepository.save(post);
    }

    @Override
    public void unflagPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));
        post.setFlagged(false);
        postRepository.save(post);
    }

    @Override
    public void incrementLikes(Long postId) {
        postRepository.incrementLikesCount(postId);
    }

    @Override
    public void incrementComments(Long postId) {
        postRepository.incrementCommentsCount(postId);
    }

    @Override
    public void incrementReports(Long postId) {
        postRepository.incrementReportCount(postId);
    }

    private PostResponse mapToResponse(Post post, UserProfile userProfile) {
        List<PostImageResponse> images = post.getImages().stream()
                .map(img -> new PostImageResponse(
                img.getId(),
                img.getUrl(),
                img.getAltText(),
                img.getDisplayOrder()
        ))
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
                post.getUpdatedAt()
        );
    }
}
