package com.kcc.groo.user.data.dto;

import com.kcc.groo.review.data.dto.ReviewResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author uyh
 * @created 2025-10-20
 * 사용자 피드 통합 정보 DTO
 */
@Data
public class UserFeedDTO {

    @Schema(description = "사용자 기본 정보")
    private UserInfo user;

    @Schema(description = "통계 정보")
    private UserStats stats;

    @Schema(description = "작성한 독후감 목록")
    private List<ReviewResponse> reviews;

    @Schema(description = "좋아요한 독후감 목록")
    private List<ReviewResponse> likedReviews;

    @Data
    public static class UserInfo {
        @Schema(description = "사용자 ID")
        private String userId;

        @Schema(description = "닉네임")
        private String nickname;

        @Schema(description = "프로필 이미지 URL")
        private String profileImage;

        @Schema(description = "자기소개")
        private String introduction;
    }

    @Data
    public static class UserStats {
        @Schema(description = "작성한 독후감 수")
        private Integer reviewCount;

        @Schema(description = "팔로워 수")
        private Integer followerCount;

        @Schema(description = "팔로잉 수")
        private Integer followingCount;
    }
}