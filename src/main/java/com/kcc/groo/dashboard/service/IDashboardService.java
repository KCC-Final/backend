package com.kcc.groo.dashboard.service;

import com.kcc.groo.dashboard.data.dto.*;

/**
 * @author uyh
 * @created 2025-01-16
 * Dashboard Service Interface
 */
public interface IDashboardService {

    /**
     * 대시보드 페이지에 필요한 전체 데이터 조회
     *
     * @param userId 로그인 사용자 ID
     * @return 대시보드 전체 데이터 DTO
     * @author YunSung
     * @created 2025-10-25
     */
    DashboardAllDataResponseDTO getDashboardAllData(String userId);

    /**
     * @param userId
     * @return
     * @author uyh
     * @created 2025-01-16
     * 대시보드 기본 통계 조회 (전체 카운트 + 카테고리)
     */
    DashboardSummaryResponse getSummaryStats(String userId);

    /**
     * @param userId
     * @param year
     * @return
     * @author uyh
     * @created 2025-01-16
     * 월별 독서량 통계 조회 (특정 연도 1-12월)
     */
    MonthlyStatsResponse getMonthlyStats(String userId, int year);

    /**
     * @param userId
     * @param year
     * @param month
     * @return
     * @author uyh
     * @created 2025-01-16
     * 월간 리포트 조회 (특정 연월 나 vs 평균)
     */
    MonthlyReportResponse getMonthlyReport(String userId, Integer year, Integer month);

    /**
     * 연도별 독후감 통계 조회
     *
     * @param userId 로그인 사용자 ID
     * @return 연도별 통계 리스트
     */
    YearlyStatsResponse getYearlyStats(String userId);

}