package com.kcc.groo.dashboard.data.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author uyh
 * @created 2025-01-16
 * 대시보드 기본 통계 Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {

    // 기본 카운트
    private int totalReviews;           // 작성한 독후감 개수
    private int totalScrappedBooks;     // 스크랩한 도서 개수
    private int totalLikedReviews;      // 좋아요 누른 독후감 개수

    // 카테고리별 통계
    private List<CategoryStat> categoryStats;
}