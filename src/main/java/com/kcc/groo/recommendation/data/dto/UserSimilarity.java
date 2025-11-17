package com.kcc.groo.recommendation.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자 간 유사도 정보 DTO
 * @author uyh
 * @created 2025-11-16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSimilarity {

    /**
     * 대상 사용자 ID
     */
    private String userId;

    /**
     * 유사도 점수 (0.0 ~ 1.0)
     */
    private Double similarity;

    /**
     * 공통으로 본 도서 수
     */
    private Integer commonBookCount;

}