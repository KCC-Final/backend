package com.kcc.groo.user.data.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kys
 * @created 2025-10-07
 * 팔로우 / 팔로잉 리스트 출력 시 함께 출력할 정보
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowUserInfoDTO {
    private int followId;
    private String userId;
    private String nickname;
    private String profileImage;
    private LocalDateTime followedAt;
}