package com.kcc.groo.dashboard.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author uyh
 * @created 2025-10-17
 * 연도별 독후감 통계 응답 DTO
 */
@Getter
@Setter
public class YearlyStatsResponse {
    private List<YearlyStat> yearlyStats;  // 연도별 통계 리스트
}
