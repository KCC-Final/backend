package com.kcc.groo.recommendation.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 도서 추천 응답 DTO
 * @author uyh
 * @created 2025-11-16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {

    /**
     * 추천된 도서의 ISBN
     */
    private String isbn;

    /**
     * 추천 점수 (높을수록 더 추천됨)
     */
    private Double score;

    /**
     * 추천 사유 (협업 필터링, 유사 도서 등)
     */
    private String reason;

    /**
     * 이 도서를 읽은 유사 사용자 수
     */
    private Integer similarUserCount;

}