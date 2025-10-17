package com.kcc.groo.challenge.data.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 특정 유저가 획득한 뱃지 정보를 담는 DTO
 * @author uyh
 */
@Getter
@Setter
@ToString
public class UserBadgeResponse {
    /**
     * 뱃지 ID
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
     * 뱃지 획득일
     */
    private LocalDateTime succeededAt;
}
