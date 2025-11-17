package com.kcc.groo.recommendation.controller;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.recommendation.data.dto.RecommendationResponse;
import com.kcc.groo.recommendation.service.IRecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 도서 추천 API 컨트롤러
 * @author uyh
 * @created 2025-11-16
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
@Tag(name = "도서 추천 API")
public class RecommendationController {

    private final IRecommendationService recommendationService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 사용자 맞춤 도서 추천
     * @param request HTTP 요청 (JWT 토큰 포함)
     * @param limit 추천할 도서 개수 (기본값: 10)
     * @return 추천 도서 목록
     * @author uyh
     * @created 2025-11-16
     */
    @Operation(summary = "사용자 맞춤 도서 추천")
    @GetMapping
    public ResponseEntity<CommonResponse<List<RecommendationResponse>>> getRecommendations(
            HttpServletRequest request,
            @RequestParam(defaultValue = "10") int limit
    ) {
        // JWT에서 사용자 ID 추출
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        log.info("도서 추천 요청 - userId: {}, limit: {}", userId, limit);

        // 추천 서비스 호출
        List<RecommendationResponse> recommendations = recommendationService
                .getRecommendations(userId, limit);

        return ResponseEntity.ok(
                new CommonResponse<>("추천 도서 조회 성공", recommendations)
        );
    }

    /**
     * 독후감 + 스크랩 기준 가장 많이 등장한 ISBN Top N 조회 API
     *
     * @param limit 조회 개수 (기본값 20)
     * @return RecommendationResponse 리스트
     */
        @GetMapping("/popular")
    public ResponseEntity<List<RecommendationResponse>> getPopularBooks(
            @RequestParam(defaultValue = "20") int limit
    ) {
        List<RecommendationResponse> result = recommendationService.getTopIsbnRecommendations(limit);
        return ResponseEntity.ok(result);
    }

}