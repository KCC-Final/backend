package com.kcc.groo.recommendation.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자의 도서 활동 정보 DTO
 * @author uyh
 * @created 2025-11-16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBookActivity {

    /**
     * 사용자 ID
     */
    private String userId;

    /**
     * 도서 ISBN
     */
    private String isbn;

    /**
     * 활동 유형 (REVIEW: 독후감, SCRAP: 스크랩)
     */
    private String activityType;

    /**
     * 활동 가중치 (독후감: 2.0, 스크랩: 1.0)
     */
    private Double weight;

    /**
     * 활동 일시
     */
    private java.time.LocalDateTime createdAt;

}