package com.kcc.groo.group.data.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 독서모임 댓글 생성/수정 요청을 위한 DTO 클래스
 *
 * @author YunSung
 * @created 2025-10-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCommentRequestDTO {

    @NotBlank(message = "[GRP-200]: 댓글 내용은 필수 입력값입니다")
    @Size(max = 500, message = "[GRP-201]: 댓글 내용은 1~500자 이내로 입력해주세요")
    private String content;

    @NotNull(message = "[GRP-205]: 공개 여부는 필수 입력값입니다")
    private Boolean flag;

    @Min(value = 1, message = "[GRP-210]: 부모 댓글 ID는 1 이상의 값이어야 합니다")
    private Integer parentId;
}
