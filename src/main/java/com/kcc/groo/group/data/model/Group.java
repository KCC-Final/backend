package com.kcc.groo.group.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kcc.groo.common.util.LocalDateToLocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 독서모임 게시글 정보를 담는 모델 클래스
 *
 * @author YunSung
 * @created 2025-10-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    private Integer groupId;  // 게시글 ID

    private String groupName;  // 모임명

    private String bookTitle;  // 도서 제목

    private Integer headcountMin;  // 모집 최소 인원

    private Integer headcountMax;  // 모집 최대 인원

    private String content;  // 내용

    private String style;  // 진행 방식

    private Boolean status;  // 모집 상태

    @JsonDeserialize(using = LocalDateToLocalDateTimeDeserializer.class)
    private LocalDateTime endDate;  // 모집 종료일

    private LocalDateTime createdAt;  // 작성일

    private LocalDateTime updatedAt;  // 수정일

    private String userId;  // 사용자 ID

    private Integer codeId;  // 코드 ID
    
    private String isbn; //isbn
}
