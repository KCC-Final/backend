package com.kcc.groo.review.data.model;

import com.kcc.groo.review.data.dto.ReviewCreateRequest;
import com.kcc.groo.review.data.dto.ReviewUpdateRequest;
import lombok.Data;

import java.util.Date;

@Data
public class Review {
    private Integer reviewId;
    private String isbn;   // ✅ 필드명도 소문자로
    private String reviewTitle;
    private String reviewContent;
    private Boolean secret;
    private Boolean status;
    private Boolean temporary;
    private Date createdAt;
    private Date updatedAt;
    private String userId;
    private String category;

    // ✅ 좋아요 개수
    private Integer likeCount;

    /** 생성 요청 → Review 엔티티 변환 */
    public static Review fromCreateRequest(String userId, ReviewCreateRequest request) {
        Review review = new Review();
        review.setUserId(userId);
        review.setIsbn(request.getIsbn());  // ✅ 올바른 getter/setter 호출
        review.setReviewTitle(request.getReviewTitle());
        review.setReviewContent(request.getReviewContent());
        review.setSecret(Boolean.TRUE.equals(request.getSecret())); 
        review.setTemporary(Boolean.TRUE.equals(request.getTemporary()));
        review.setStatus(true);
        review.setCategory(request.getCategory());
        return review;
    }

    /** 수정 요청 → Review 엔티티 변환 */
    public static Review fromUpdateRequest(String userId, Integer reviewId, ReviewUpdateRequest request) {
        Review review = new Review();
        review.setReviewId(reviewId);
        review.setUserId(userId);
        if (request.getReviewTitle() != null) review.setReviewTitle(request.getReviewTitle());
        if (request.getReviewContent() != null) review.setReviewContent(request.getReviewContent());
        if (request.getSecret() != null) review.setSecret(request.getSecret());
        if (request.getTemporary() != null) review.setTemporary(request.getTemporary());
        review.setUpdatedAt(new Date());
        return review;
    }
}
