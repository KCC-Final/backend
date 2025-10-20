package com.kcc.groo.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.dashboard.data.dto.DashboardSummaryResponse;
import com.kcc.groo.dashboard.data.dto.MonthlyReportResponse;
import com.kcc.groo.dashboard.data.dto.MonthlyStatsResponse;
import com.kcc.groo.dashboard.data.dto.YearlyStatsResponse;
import com.kcc.groo.dashboard.service.IDashboardService;
import com.kcc.groo.jwt.JwtTokenProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author uyh
 * @created 2025-01-16
 * Dashboard Controller
 */
@Tag(name = "Dashboard", description = "대시보드 통계 API")
@RestController
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "대시보드 기본 통계 조회",
            description = "작성한 독후감 개수, 스크랩한 도서 개수, 좋아요 누른 독후감 개수, 카테고리별 독서 통계를 조회합니다.")
    @GetMapping("/summary")
    public ResponseEntity<CommonResponse<DashboardSummaryResponse>> getSummaryStats(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        DashboardSummaryResponse summaryStats = dashboardService.getSummaryStats(userId);
        return ResponseEntity.ok(new CommonResponse<>("대시보드 기본 통계 조회 성공", summaryStats));
    }

    @Operation(summary = "연도별 월별 독서량 통계 조회",
            description = "특정 연도의 1월부터 12월까지 월별 독후감 작성 통계를 조회합니다.")
    @GetMapping("/monthly")
    public ResponseEntity<CommonResponse<MonthlyStatsResponse>> getMonthlyStats(
            HttpServletRequest request,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Integer year) {

        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        if (year == null) {
            year = java.time.LocalDate.now().getYear();
        }

        MonthlyStatsResponse monthlyStats = dashboardService.getMonthlyStats(userId, year);
        return ResponseEntity.ok(new CommonResponse<>("월별 독서량 통계 조회 성공", monthlyStats));
    }

    @Operation(summary = "월간 리포트 조회",
            description = "특정 연월의 나의 독서량과 전체 사용자 평균 독서량을 비교합니다.")
    @GetMapping("/monthly-report")
    public ResponseEntity<CommonResponse<MonthlyReportResponse>> getMonthlyReport(
            HttpServletRequest request,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Integer year,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Integer month) {

        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        MonthlyReportResponse monthlyReport = dashboardService.getMonthlyReport(userId, year, month);
        return ResponseEntity.ok(new CommonResponse<>("월간 리포트 조회 성공", monthlyReport));
    }

    // ======================================================
    // 연도별 독서량 통계 조회 추가
    // ======================================================
    @Operation(summary = "연도별 독서량 통계 조회",
            description = "최근 5년간의 연도별 독후감 작성 통계를 조회합니다.")
    @GetMapping("/yearly")
    public ResponseEntity<CommonResponse<YearlyStatsResponse>> getYearlyStats(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        YearlyStatsResponse yearlyStats = dashboardService.getYearlyStats(userId);
        return ResponseEntity.ok(new CommonResponse<>("연도별 독서량 통계 조회 성공", yearlyStats));
    }
}
