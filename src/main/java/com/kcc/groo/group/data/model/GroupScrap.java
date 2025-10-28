package com.kcc.groo.group.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 독서모임 스크랩 정보를 담는 모델 클래스
 *
 * @author YunSung
 * @created 2025-10-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupScrap {

    private String userId;  // 사용자 ID

    private int groupId;  // 독서 모임 게시글 ID

    private LocalDateTime createdAt;  // 스크랩 생성 일시
}
