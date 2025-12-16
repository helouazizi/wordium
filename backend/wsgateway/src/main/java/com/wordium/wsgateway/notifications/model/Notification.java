package com.wordium.wsgateway.notifications.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long receiverUserId;

    @Column(nullable = false)
    private Long actorUserId;

    // FOLLOW, UNFOLLOW, LIKE, COMMENT
    @Column(nullable = false, length = 50)
    private String type;

    // Optional: postId, commentId, etc.
    @Column
    private Long referenceId;

    @Column(nullable = false)
    private boolean isRead = false;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Notification() {
        this.createdAt = Instant.now();
    }

    public Notification(
            Long receiverUserId,
            Long actorUserId,
            String type,
            Long referenceId
    ) {
        this.receiverUserId = receiverUserId;
        this.actorUserId = actorUserId;
        this.type = type;
        this.referenceId = referenceId;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getReceiverUserId() {
        return receiverUserId;
    }

    public Long getActorUserId() {
        return actorUserId;
    }

    public String getType() {
        return type;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public boolean isRead() {
        return isRead;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
