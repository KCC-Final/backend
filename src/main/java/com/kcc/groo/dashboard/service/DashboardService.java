package com.kcc.groo.dashboard.service;

import com.kcc.groo.common.exception.GrooException;
import com.kcc.groo.dashboard.dao.IDashboardRepository;
import com.kcc.groo.dashboard.data.dto.*;
import com.kcc.groo.dashboard.exception.DashboardErrorCode;
import com.kcc.groo.user.dao.IFollowsRepository;
import com.kcc.groo.user.data.dto.FollowUserInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    @Autowired
    private IFollowsRepository iFollowsRepository;

    @Override
    public DashboardAllDataResponseDTO getDashboardAllData(String userId) {
        // 현재 연월 정보
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        // 팔로워 및 팔로잉 목록 조회
        List<FollowUserInfoDTO> followers = iFollowsRepository.selectFollowerList(userId);
        List<FollowUserInfoDTO> followings = iFollowsRepository.selectFollowingList(userId);

        // 작성한 독후감 개수, 스크랩한 도서 개수, 좋아요 누른 독후감 개수 조회
        int totalReviews = dashboardRepository.countReviewsByUser(userId);
        int totalScrappedBooks = dashboardRepository.countScrappedBooks(userId);
        int totalLikedReviews = dashboardRepository.countLikedReviews(userId);

        // 월별, 연도별 독서량 통계
        List<MonthlyStat> monthlyStats = dashboardRepository.getMonthlyReviewStats(userId, java.time.LocalDate.now().getYear());
        List<YearlyStat> yearlyStats = dashboardRepository.findYearlyStats(userId);

        // 카테고리별 독서 통계 조회
        List<CategoryStat> categoryStats = dashboardRepository.getCategoryStats(userId);

        // 월간 리포트 정보 조회
        int myAverage = dashboardRepository.getMonthlyReviewCount(userId, year, month);
        Double AllUserAverage = dashboardRepository.getAverageMonthlyReviewCount(year, month);
        double totalAverage = (AllUserAverage != null) ? AllUserAverage : 0.0;

        // 대시보드 전체 데이터 응답 DTO 생성 및 반환
        return DashboardAllDataResponseDTO.builder()
                .followers(followers)
                .followings(followings)
                .totalReviews(totalReviews)
                .totalScrappedBooks(totalScrappedBooks)
                .totalLikedReviews(totalLikedReviews)
                .monthlyStats(monthlyStats)
                .yearlyStats(yearlyStats)
                .categoryStats(categoryStats)
                .reportInfo(DashboardAllDataResponseDTO.ReportInfo.builder()
                        .myAverage(myAverage)
                        .totalAverage(totalAverage)
                        .year(year)
                        .month(month)
                        .build())
                .build();
    }

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
            throw new GrooException(DashboardErrorCode.SUMMARY_STATS_FAILED, e.getMessage());
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
            throw new GrooException(DashboardErrorCode.MONTHLY_STATS_FAILED, e.getMessage());
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
            throw new GrooException(DashboardErrorCode.MONTHLY_REPORT_FAILED, e.getMessage());
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
            throw new GrooException(DashboardErrorCode.INVALID_USER_ID);
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
            throw new GrooException(DashboardErrorCode.YEAR_OUT_OF_RANGE);
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
            throw new GrooException(DashboardErrorCode.INVALID_MONTH);
        }
    }


    /**
     * 최근 5년간의 연도별 독후감 통계 조회
     */
    @Override
    public YearlyStatsResponse getYearlyStats(String userId) {
        List<YearlyStat> stats = dashboardRepository.findYearlyStats(userId);

        YearlyStatsResponse response = new YearlyStatsResponse();
        response.setYearlyStats(stats);
        return response;
    }

}
