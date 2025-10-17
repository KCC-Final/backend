package com.kcc.groo.dashboard.data.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author uyh
 * @created 2025-10-17
 * 연도별 독후감 통계 DTO
 * - 연도별 독후감 개수를 담음
 */
@Getter
@Setter
public class YearlyStat {
    private int year;     // 연도
    private long count;   // 해당 연도의 독후감 개수
}
