package com.kcc.groo.review.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 독후감과 사용자가 작성한 댓글 정보를 함께 담는 DTO
 *
 * @author uyh
 * @created 2025-10-28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewWithCommentResponseDto {
    // 독후감 정보
    private Integer reviewId;
    private String isbn;
    private String reviewTitle;
    private String reviewContent;
    private String category;
    private String createdAt;
    private String updatedAt;
    private String userId;
    private String authorNickname;
    private String authorProfileImage;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean liked;
    private String customThumbnail;  // 추가

    // 내가 작성한 댓글 정보
    private Integer myCommentId;
    private String myCommentContent;
    private String myCommentCreatedAt;
    private String myCommentUpdatedAt;
}