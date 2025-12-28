package com.wordium.posts.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
        name = "post_reactions",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"post_id", "user_id"})
        }
)
public class PostReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id")
    private Long userId;

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getPostId() {
        return this.postId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }
}
