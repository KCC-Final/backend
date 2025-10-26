package com.kcc.groo.challenge.data.dto;

import com.kcc.groo.challenge.data.model.Badge;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자의 뱃지 획득 상태를 포함한 뱃지 정보 응답 DTO
 *
 * @author uyh
 * @created 2025-10-16
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자의 뱃지 획득 상태를 포함한 뱃지 정보 응답 DTO")
public class UserBadgeStatusResponse {

    @Schema(description = "뱃지 ID", example = "1")
    private Integer badgeId;

    @Schema(description = "뱃지 이름", example = "첫걸음")
    private String badgeName;

    @Schema(description = "뱃지 설명", example = "첫 리뷰를 작성하면 획득할 수 있습니다.")
    private String badgeDescription;

    @Schema(description = "뱃지 획득 조건 (목표치)", example = "10")
    private Integer badgeConditions;

    @Schema(description = "현재 진행도", example = "7")
    private Integer currentProgress;

    @Schema(description = "뱃지 획득 여부", example = "true")
    private boolean acquired;

    @Schema(description = "뱃지 획득 날짜 (획득하지 않았을 경우 null)", example = "2025-10-16T10:30:00")
    private LocalDateTime acquiredDate;

    /**
     * @param badge           뱃지 엔티티
     * @param currentProgress 현재 진행도
     * @param acquired        획득 여부
     * @param acquiredDate    획득 날짜
     * @return UserBadgeStatusResponse
     * @author uyh
     * @created 2025-10-16
     * Badge 엔티티로부터 UserBadgeStatusResponse DTO를 생성
     */
    public static UserBadgeStatusResponse from(Badge badge, Integer currentProgress, boolean acquired, LocalDateTime acquiredDate) {
        return UserBadgeStatusResponse.builder()
                .badgeId(badge.getBadgeId())
                .badgeName(badge.getBadgeName())
                .badgeDescription(badge.getBadgeDescription())
                .badgeConditions(badge.getBadgeConditions())
                .currentProgress(currentProgress)
                .acquired(acquired)
                .acquiredDate(acquiredDate)
                .build();
    }
}