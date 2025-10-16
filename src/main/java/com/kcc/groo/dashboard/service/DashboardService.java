package com.kcc.groo.dashboard.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcc.groo.dashboard.dao.IDashboardRepository;
import com.kcc.groo.dashboard.data.dto.CategoryStat;
import com.kcc.groo.dashboard.data.dto.DashboardSummaryResponse;
import com.kcc.groo.dashboard.data.dto.MonthlyStat;
import com.kcc.groo.dashboard.data.dto.MonthlyStatsResponse;
import com.kcc.groo.dashboard.data.dto.MonthlyReportResponse;

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

        // 1. 작성한 독후감 개수
        int totalReviews = dashboardRepository.countReviewsByUser(userId);

        // 2. 스크랩한 도서 개수
        int totalScrappedBooks = dashboardRepository.countScrappedBooks(userId);

        // 3. 좋아요 누른 독후감 개수
        int totalLikedReviews = dashboardRepository.countLikedReviews(userId);

        // 4. 카테고리별 독서 통계
        List<CategoryStat> categoryStats = dashboardRepository.getCategoryStats(userId);

        log.info("Dashboard summary stats retrieved for user: {}", userId);

        return new DashboardSummaryResponse(
                totalReviews,
                totalScrappedBooks,
                totalLikedReviews,
                categoryStats
        );
    }

    @Override
    public MonthlyStatsResponse getMonthlyStats(String userId, int year) {

        // 월별 독서량 통계 (특정 연도 1-12월)
        List<MonthlyStat> monthlyStats = dashboardRepository.getMonthlyReviewStats(userId, year);

        log.info("Monthly stats retrieved for user: {} (year: {})", userId, year);

        return new MonthlyStatsResponse(monthlyStats);
    }

    @Override
    public MonthlyReportResponse getMonthlyReport(String userId, Integer year, Integer month) {

        // year, month가 null이면 현재 연월 사용
        LocalDate now = LocalDate.now();
        if (year == null) {
            year = now.getYear();
        }
        if (month == null) {
            month = now.getMonthValue();
        }

        // 1. 내 독서량
        int myCount = dashboardRepository.getMonthlyReviewCount(userId, year, month);

        // 2. 전체 사용자 평균
        Double averageCount = dashboardRepository.getAverageMonthlyReviewCount(year, month);

        // 평균값이 null인 경우 0.0으로 처리
        double avgCount = (averageCount != null) ? averageCount : 0.0;

        log.info("Monthly report retrieved for user: {} (year: {}, month: {})", userId, year, month);

        return new MonthlyReportResponse(myCount, avgCount, year, month);
    }
}