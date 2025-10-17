package com.kcc.groo.dashboard.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author uyh
 * @created 2025-01-16
 * 월별 독서량 통계 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStat {
    private String month;  // YYYY-MM 형식
    private int count;     // 해당 월 독후감 개수
}