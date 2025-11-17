package com.kcc.groo.recommendation.service;

import com.kcc.groo.recommendation.data.dto.RecommendationResponse;

import java.util.List;

/**
 * 추천 시스템 서비스 인터페이스
 * @author uyh
 * @created 2025-11-16
 */
public interface IRecommendationService {

    /**
     * 특정 사용자에게 맞춤 도서를 추천
     * @param userId 사용자 ID
     * @param limit 추천할 도서 개수
     * @return 추천 도서 목록
     * @author uyh
     * @created 2025-11-16
     */
    List<RecommendationResponse> getRecommendations(String userId, int limit);


    /**
     * 사이트 전체에서 가장 많이 등장한 ISBN 기반 추천 목록 조회
     */
    List<RecommendationResponse> getTopIsbnRecommendations(int limit);

}