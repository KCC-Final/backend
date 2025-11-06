package com.kcc.groo.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 도서 정보 응답을 위한 DTO 클래스
 *
 * @author YunSung
 * @created 2025-11-07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookInfoResponseDTO {

    private int reviewCount;  // 작성된 독후감 수

    private int scrapCount;  // 스크랩 수
}
