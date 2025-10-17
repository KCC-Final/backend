package com.kcc.groo.dashboard.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcc.groo.dashboard.dao.IDashboardRepository;
import com.kcc.groo.dashboard.data.dto.CategoryStat;
import com.kcc.groo.dashboard.data.dto.DashboardSummaryResponse;
import com.kcc.groo.dashboard.data.dto.MonthlyStat;
import com.kcc.groo.dashboard.data.dto.MonthlyStatsResponse;
import com.kcc.groo.dashboard.data.dto.MonthlyReportResponse;
import com.kcc.groo.dashboard.exception.DashboardErrorCode;
import com.kcc.groo.dashboard.exception.DashboardException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author uyh
 * @created 2025-01-16
 * Dashboard Service Implementation
 */
@Service
@Slf4j
public class DashboardService implements IDashboardService {

    @Autowired
    private IDashboardRepository dashboardRepository;

    @Override
    public DashboardSummaryResponse getSummaryStats(String userId) {
        // 입력 검증
        validateUserId(userId);

        try {
            // 1. 작성한 독후감 개수
            int totalReviews = dashboardRepository.countReviewsByUser(userId);

            // 2. 스크랩한 도서 개수
            int totalScrappedBooks = dashboardRepository.countScrappedBooks(userId);

            // 3. 좋아요 누른 독후감 개수
            int totalLikedReviews = dashboardRepository.countLikedReviews(userId);

            // 4. 카테고리별 독서 통계
            List<CategoryStat> categoryStats = dashboardRepository.getCategoryStats(userId);

            log.info("[getSummaryStats] Dashboard summary stats retrieved for user: {}", userId);

            return new DashboardSummaryResponse(
                    totalReviews,
                    totalScrappedBooks,
                    totalLikedReviews,
                    categoryStats
            );
        } catch (Exception e) {
            log.error("[getSummaryStats] Failed - userId: {}, error: {}", userId, e.getMessage());
            throw new DashboardException(DashboardErrorCode.SUMMARY_STATS_FAILED, e.getMessage());
        }
    }

    @Override
    public MonthlyStatsResponse getMonthlyStats(String userId, int year) {
        // 입력 검증
        validateUserId(userId);
        validateYear(year);

        try {
            // 월별 독서량 통계 (특정 연도 1-12월)
            List<MonthlyStat> monthlyStats = dashboardRepository.getMonthlyReviewStats(userId, year);

            log.info("[getMonthlyStats] Monthly stats retrieved for user: {} (year: {})", userId, year);

            return new MonthlyStatsResponse(monthlyStats);
        } catch (Exception e) {
            log.error("[getMonthlyStats] Failed - userId: {}, year: {}, error: {}", userId, year, e.getMessage());
            throw new DashboardException(DashboardErrorCode.MONTHLY_STATS_FAILED, e.getMessage());
        }
    }

    @Override
    public MonthlyReportResponse getMonthlyReport(String userId, Integer year, Integer month) {
        // 입력 검증
        validateUserId(userId);

        // year, month가 null이면 현재 연월 사용
        LocalDate now = LocalDate.now();
        if (year == null) {
            year = now.getYear();
        }
        if (month == null) {
            month = now.getMonthValue();
        }

        validateYear(year);
        validateMonth(month);

        try {
            // 1. 내 독서량
            int myCount = dashboardRepository.getMonthlyReviewCount(userId, year, month);

            // 2. 전체 사용자 평균
            Double averageCount = dashboardRepository.getAverageMonthlyReviewCount(year, month);

            // 평균값이 null인 경우 0.0으로 처리
            double avgCount = (averageCount != null) ? averageCount : 0.0;

            log.info("[getMonthlyReport] Monthly report retrieved for user: {} (year: {}, month: {})",
                    userId, year, month);

            return new MonthlyReportResponse(myCount, avgCount, year, month);
        } catch (Exception e) {
            log.error("[getMonthlyReport] Failed - userId: {}, year: {}, month: {}, error: {}",
                    userId, year, month, e.getMessage());
            throw new DashboardException(DashboardErrorCode.MONTHLY_REPORT_FAILED, e.getMessage());
        }
    }

    // ============ Private Validation Methods ============

    /**
     * @param userId
     * @author uyh
     * @created 2025-01-16
     * 사용자 ID 검증
     */
    private void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new DashboardException(DashboardErrorCode.INVALID_USER_ID);
        }
    }

    /**
     * @param year
     * @author uyh
     * @created 2025-01-16
     * 연도 검증
     */
    private void validateYear(int year) {
        if (year < 1900 || year > 2100) {
            throw new DashboardException(DashboardErrorCode.YEAR_OUT_OF_RANGE);
        }
    }

    /**
     * @param month
     * @author uyh
     * @created 2025-01-16
     * 월 검증
     */
    private void validateMonth(int month) {
        if (month < 1 || month > 12) {
            throw new DashboardException(DashboardErrorCode.INVALID_MONTH);
        }
    }
}