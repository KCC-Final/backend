package com.kcc.groo.dashboard.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author uyh
 * @created 2025-01-16
 * 월간 리포트 Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReportResponse {
    private int myCount;          // 나의 해당 월 독서량
    private double averageCount;  // 전체 사용자 평균 독서량
    private int year;             // 연도
    private int month;            // 월 (1-12)
}