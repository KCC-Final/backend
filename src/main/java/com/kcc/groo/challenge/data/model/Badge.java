package com.kcc.groo.challenge.data.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * `badges` 테이블에 매핑되는 뱃지 엔티티 클래스.
 * @author uyh
 * @created 2025-10-16
 */
@Getter
@Setter
@ToString
public class Badge {
    /**
     * 뱃지 ID (Primary Key)
     */
    private Integer badgeId;
    /**
     * 뱃지 이름
     */
    private String badgeName;
    /**
     * 뱃지 설명
     */
    private String badgeDescription;
    /**
     * 뱃지 달성 조건 (횟수 등)
     */
    private Integer badgeConditions;
}