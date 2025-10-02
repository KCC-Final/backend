package com.kcc.groo.review.data.dto;

import com.kcc.groo.review.data.model.Review;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
public class ReviewResponse {
    private Integer reviewId;
    private String isbn;          // ✅ 소문자 필드로 변경 (Review와 일치)
    private String reviewTitle;
    private String reviewContent;
    private Boolean secret;
    private Boolean temporary;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userId;
    private String category;      
    private Integer likeCount;    // 좋아요 개수
    private Boolean liked;        // 현재 로그인 유저가 좋아요 눌렀는지 여부
    private List<CommentResponse> comments;  // ✅ 댓글 포함

    

    public static ReviewResponse fromModel(Review review) {
        ReviewResponse res = new ReviewResponse();
        res.setReviewId(review.getReviewId());
        res.setIsbn(review.getIsbn()); // ✅ 수정: getIsbn() 호출
        res.setReviewTitle(review.getReviewTitle());
        res.setReviewContent(review.getReviewContent());
        res.setSecret(review.getSecret());
        res.setTemporary(review.getTemporary());

        // ✅ Date → LocalDateTime 변환
        if (review.getCreatedAt() != null) {
            res.setCreatedAt(review.getCreatedAt().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        }
        if (review.getUpdatedAt() != null) {
            res.setUpdatedAt(review.getUpdatedAt().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime());
        }

        res.setUserId(review.getUserId());
        res.setCategory(review.getCategory());
        res.setLikeCount(review.getLikeCount()); // ✅ 좋아요 개수 매핑
        
        return res;
    }
}
