package model;

import java.time.LocalDateTime;

public class Review {
    private int reviewId;
    private int customerId;
    private Integer itemId; // nullable — null means restaurant overall review
    private int starRating;
    private String comment;
    private LocalDateTime createdAt;

    public Review() {
    }

    public Review(int customerId, Integer itemId, int starRating, String comment) {
        this.customerId = customerId;
        this.itemId = itemId;
        this.starRating = starRating;
        this.comment = comment;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}