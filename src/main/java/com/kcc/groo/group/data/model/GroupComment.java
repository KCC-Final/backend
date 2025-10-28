package com.kcc.groo.group.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 독서모임 댓글 정보를 담는 모델 클래스
 *
 * @author YunSung
 * @created 2025-10-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupComment {

    private int commentId;  // 댓글 ID

    private String content;  // 댓글 내용

    private LocalDateTime createdAt;  // 작성일

    private LocalDateTime updatedAt;  // 수정일

    private Boolean flag;  // 비밀 댓글 여부

    private int groupId;  // 게시글 ID

    private String userId;  // 사용자 ID

    private Integer parentId;  // 부모 댓글 ID
}
