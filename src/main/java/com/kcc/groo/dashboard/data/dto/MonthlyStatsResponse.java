package com.kcc.groo.dashboard.data.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author uyh
 * @created 2025-01-16
 * 월별 독서량 통계 Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStatsResponse {
    private List<MonthlyStat> monthlyStats;
}