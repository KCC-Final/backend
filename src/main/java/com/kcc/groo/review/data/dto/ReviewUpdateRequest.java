package com.kcc.groo.review.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReviewUpdateRequest {

    @Schema(description = "제목", example = "수정된 독후감 제목")
    private String reviewTitle;

    @Schema(description = "내용", example = "수정된 독후감 내용")
    private String reviewContent;

    @Schema(description = "비밀글 여부", example = "false")
    private Boolean secret;

    @Schema(description = "임시저장 여부", example = "false")
    private Boolean temporary;

    @Schema(description = "커스텀 썸네일 (base64)", example = "data:image/jpeg;base64,/9j/4AAQSkZJRg...")
    private String customThumbnail;
}
