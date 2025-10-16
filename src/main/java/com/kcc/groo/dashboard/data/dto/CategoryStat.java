package com.kcc.groo.dashboard.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author uyh
 * @created 2025-01-16
 * 카테고리별 독서 통계 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryStat {
    private String category;  // 카테고리명
    private int count;        // 해당 카테고리 독후감 개수
}