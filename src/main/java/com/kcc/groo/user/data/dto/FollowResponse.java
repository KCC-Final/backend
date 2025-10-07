package com.kcc.groo.user.data.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author kys
 * @created 2025-10-07
 * 팔로우 요청 후 응답
 */
@Data
@AllArgsConstructor
public class FollowResponse {
    private int followId;
    private String follower;
    private String followed;
    private boolean mutual; // 맞팔 여부
    private LocalDateTime createdAt;
}
