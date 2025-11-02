package com.kcc.groo.group.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kcc.groo.common.util.LocalDateToLocalDateTimeDeserializer;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 모임 생성/수정 요청을 위한 DTO 클래스입니다.
 * <p>
 * 검증 어노테이션을 사용하여 클라이언트로부터 받은 입력값의 유효성을 검사합니다.
 *
 * @author YunSung
 * @created 2025-10-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRequestDTO {

    @NotBlank(message = "[GRP-100]: 게시글 제목은 필수 입력값입니다")
    @Size(max = 100, message = "[GRP-101]: 게시글 제목은 1~100자 이내로 입력해주세요")
    private String groupName;

    @Size(max = 200, message = "[GRP-105]: 독서모임 도서명은 1~200자 이내로 입력해주세요")
    private String bookTitle;
    
    private String isbn; //isbn -> 도서 선택 시 자동 주입?

    @Min(value = 1, message = "[GRP-110]: 모집 최소 인원은 1명 이상이어야 합니다")
    private Integer headcountMin;

    @Min(value = 1, message = "[GRP-115]: 모집 최대 인원은 1명 이상이어야 합니다")
    @Max(value = 12, message = "[GRP-116]: 모집 최대 인원은 12명 이하이어야 합니다")
    private Integer headcountMax;

    @NotBlank(message = "[GRP-120]: 게시글 내용은 필수 입력값입니다")
    @Size(max = 1000, message = "[GRP-121]: 게시글 내용은 1~1000자 이내로 입력해주세요")
    private String content;

    @Pattern(regexp = "^(독서|토론|자유)$", message = "[GRP-125]: 모임 스타일은 '자유', '독서', '토론' 중 하나여야 합니다")
    private String style;

    @NotNull(message = "[GRP-130]: 모집 상태는 필수 입력값입니다")
    private Boolean status;

    @NotNull(message = "[GRP-135]: 모집 마감일은 필수 입력값입니다")
    @Future(message = "[GRP-136]: 모집 마감일은 현재 날짜 이후여야 합니다")
    @JsonDeserialize(using = LocalDateToLocalDateTimeDeserializer.class)
    private LocalDateTime endDate;

    @NotNull(message = "[GRP-140]: 모임의 지역 코드는 필수 입력값입니다")
    @Min(value = 1, message = "[GRP-141]: 모임의 지역 코드는 1 이상의 값이어야 합니다")
    @Max(value = 17, message = "[GRP-142]: 모임의 지역 코드는 17 이하의 값이어야 합니다")
    private Integer codeId;

    /**
     * headcountMin과 headcountMax의 유효성을 검사합니다.
     * (두 값이 모두 존재할 경우, max는 min보다 크거나 같아야 합니다)
     */
    @AssertTrue(message = "[GRP-117]: 모집 최대 인원은 최소 인원보다 크거나 같아야 합니다")
    @JsonIgnore
    public boolean isHeadcountRangeValid() {
        // headcountMin 또는 headcountMax가 null이면 (값이 없으면) 통과
        if (headcountMin == null || headcountMax == null) {
            return true;
        }

        // 두 값(min, max)이 모두 존재하면, max가 min보다 크거나 같은지(>=) 확인합니다.
        return headcountMax >= headcountMin;
    }
}
