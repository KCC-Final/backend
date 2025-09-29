package com.kcc.groo.review.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommentRequest {
    private String content;   // 댓글 내용
    @Schema(description = "부모 댓글 ID (null이면 최상위 댓글)", example = "null")
    private Integer parentId; // 대댓글인 경우 부모 댓글 ID (null 가능)
}