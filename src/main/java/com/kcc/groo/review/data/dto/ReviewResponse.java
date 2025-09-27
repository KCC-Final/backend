package com.kcc.groo.review.data.dto;

import com.kcc.groo.review.data.model.Review;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponse {
    private Integer reviewId;
    private String ISBN;
    private String reviewTitle;
    private String reviewContent;
    private Integer viewCnt;
    private Boolean secret;
    private Boolean temporary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userId;

    public static ReviewResponse fromModel(Review review) {
        ReviewResponse res = new ReviewResponse();
        res.setReviewId(review.getReviewId());
        res.setISBN(review.getISBN());
        res.setReviewTitle(review.getReviewTitle());
        res.setReviewContent(review.getReviewContent());
        res.setViewCnt(review.getViewCnt());
        res.setSecret(review.getSecret());
        res.setTemporary(review.getTemporary());
        res.setCreatedAt(review.getCreatedAt());
        res.setUpdatedAt(review.getUpdatedAt());
        res.setUserId(review.getUserId());
        return res;
    }
}
