package com.kcc.groo.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.dashboard.data.dto.DashboardSummaryResponse;
import com.kcc.groo.dashboard.data.dto.MonthlyStatsResponse;
import com.kcc.groo.dashboard.data.dto.MonthlyReportResponse;
import com.kcc.groo.dashboard.service.IDashboardService;
import com.kcc.groo.jwt.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

/**
 * @author uyh
 * @created 2025-01-16
 * Dashboard Controller
 */
@RestController
@RequestMapping("api/v1/dashboard")
@Slf4j
public class DashboardController {

    @Autowired
    private IDashboardService dashboardService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * @param request
     * @return
     * @author uyh
     * @created 2025-01-16
     * 대시보드 기본 통계 조회 (전체 카운트 + 카테고리)
     */
    @GetMapping("/summary")
    public ResponseEntity<CommonResponse<DashboardSummaryResponse>> getSummaryStats(HttpServletRequest request) {

        try {
            // JWT에서 userId 추출
            String accessToken = jwtTokenProvider.resolveAccessToken(request);
            String userId = jwtTokenProvider.getUserId(accessToken);

            // 기본 통계 조회
            DashboardSummaryResponse summaryStats = dashboardService.getSummaryStats(userId);

            return ResponseEntity.ok(
                    new CommonResponse<>("대시보드 기본 통계 조회 성공", summaryStats)
            );

        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse<>(e.getMessage(), null));

        } catch (Exception e) {
            log.error("Dashboard summary stats retrieval failed", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse<>("대시보드 기본 통계 조회 실패", null));
        }
    }

    /**
     * @param request
     * @param year
     * @return
     * @author uyh
     * @created 2025-01-16
     * 월별 독서량 통계 조회 (특정 연도 1-12월)
     */
    @GetMapping("/monthly")
    public ResponseEntity<CommonResponse<MonthlyStatsResponse>> getMonthlyStats(
            HttpServletRequest request,
            @RequestParam(required = false) Integer year) {

        try {
            // JWT에서 userId 추출
            String accessToken = jwtTokenProvider.resolveAccessToken(request);
            String userId = jwtTokenProvider.getUserId(accessToken);

            // year가 null이면 현재 연도 사용
            if (year == null) {
                year = LocalDate.now().getYear();
            }

            // year 유효성 검증 (1900 ~ 현재+1년)
            int currentYear = LocalDate.now().getYear();
            if (year < 1900 || year > currentYear + 1) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new CommonResponse<>("연도는 1900 ~ " + (currentYear + 1) + " 사이여야 합니다", null));
            }

            // 월별 통계 조회
            MonthlyStatsResponse monthlyStats = dashboardService.getMonthlyStats(userId, year);

            return ResponseEntity.ok(
                    new CommonResponse<>("월별 독서량 통계 조회 성공", monthlyStats)
            );

        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse<>(e.getMessage(), null));

        } catch (Exception e) {
            log.error("Monthly stats retrieval failed", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse<>("월별 독서량 통계 조회 실패", null));
        }
    }

    /**
     * @param request
     * @param year
     * @param month
     * @return
     * @author uyh
     * @created 2025-01-16
     * 월간 리포트 조회 (특정 연월 나 vs 평균)
     */
    @GetMapping("/monthly-report")
    public ResponseEntity<CommonResponse<MonthlyReportResponse>> getMonthlyReport(
            HttpServletRequest request,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        try {
            // JWT에서 userId 추출
            String accessToken = jwtTokenProvider.resolveAccessToken(request);
            String userId = jwtTokenProvider.getUserId(accessToken);

            // year, month 유효성 검증
            LocalDate now = LocalDate.now();
            int currentYear = now.getYear();
            int currentMonth = now.getMonthValue();

            // year가 제공된 경우 유효성 검증
            if (year != null) {
                if (year < 1900 || year > currentYear + 1) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new CommonResponse<>("연도는 1900년부터 " + (currentYear + 1) + "년까지 가능합니다", null));
                }
            }

            // month가 제공된 경우 유효성 검증
            if (month != null) {
                if (month < 1 || month > 12) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new CommonResponse<>("월은 1부터 12까지 가능합니다", null));
                }
            }

            // 미래 날짜 체크 (year와 month가 모두 제공된 경우)
            if (year != null && month != null) {
                if (year > currentYear || (year == currentYear && month > currentMonth)) {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new CommonResponse<>("미래 날짜는 조회할 수 없습니다", null));
                }
            }

            // 월간 리포트 조회
            MonthlyReportResponse monthlyReport = dashboardService.getMonthlyReport(userId, year, month);

            return ResponseEntity.ok(
                    new CommonResponse<>("월간 리포트 조회 성공", monthlyReport)
            );

        } catch (IllegalArgumentException e) {
            log.error("Invalid request: {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new CommonResponse<>(e.getMessage(), null));

        } catch (Exception e) {
            log.error("Monthly report retrieval failed", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommonResponse<>("월간 리포트 조회 실패", null));
        }
    }
}