package com.kcc.groo.review.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponse {

    @Schema(description = "댓글 ID")
    private Integer commentId;

    @Schema(description = "댓글 내용")
    private String content;

    @Schema(description = "작성자 ID")
    private String userId;

    @Schema(description = "작성일")
    private LocalDateTime createdAt;

    @Schema(description = "수정일")
    private LocalDateTime updatedAt;

    @Schema(description = "부모 댓글 ID (없으면 null)")
    private Integer parentId;
    
    @Schema(description = "댓글이 달린 독후감 ID")
    private Integer reviewId;
    
    @Schema(description = "댓글이 달린 독후감 제목")
    private String reviewTitle;
    
    @Schema(description = "본인이 작성한 댓글인지 확인")
    private Boolean isOwner;  // 추가

    // 추가
    @Schema(description = "작성자 닉네임")
    private String authorNickname;

    @Schema(description = "작성자 프로필 이미지")
    private String authorProfileImage;

}
