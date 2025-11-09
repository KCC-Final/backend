package com.kcc.groo.review.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReviewCreateRequest {

    @Schema(description = "ISBN", example = "9788996991342")
    private String isbn;

    @Schema(description = "제목", example = "해리포터 감상문")
    private String reviewTitle;

    @Schema(description = "내용", example = "책이 정말 재미있었습니다...")
    private String reviewContent;

    @Schema(description = "비밀글 여부", example = "false")
    private Boolean secret;

    @Schema(description = "임시저장 여부", example = "false")
    private Boolean temporary;

    @Schema(description = "카테고리", example = "1")
    private String category;

    @Schema(description = "커스텀 썸네일 (base64)", example = "data:image/jpeg;base64,/9j/4AAQSkZJRg...")
    private String customThumbnail;
}
