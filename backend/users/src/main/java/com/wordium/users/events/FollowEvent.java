package com.wordium.users.events;

import java.time.Instant;

public class FollowEvent {

    private String followerId;
    private String followedId;
    private String action; // FOLLOW or UNFOLLOW
    private Instant timestamp;

    public FollowEvent(String followerId, String followedId, String action) {
        this.followerId = followerId;
        this.followedId = followedId;
        this.action = action;
        this.timestamp = Instant.now();
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getFollowerId() {
        return this.followerId;
    }

    public void setFollowedId(String followedId) {
        this.followedId = followedId;
    }

    public String getFollowedId() {
        return this.followedId;
    }

}
