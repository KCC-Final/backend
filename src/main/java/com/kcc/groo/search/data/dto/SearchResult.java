package com.kcc.groo.search.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kys
 * @created 2025-10-22
 * 검색 결과 반환 dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult {
	private String category; // USER, REVIEW, COMMENT
    private String id;       // userId, reviewId, commentId
    private String title;    // 대표 텍스트 (이름, 독후감 제목, 댓글 내용)
    private String subtext;  // 부가 정보 (닉네임, 이메일, 아이디, 이름, 책 제목... etc)
    private String content;  // 본문 요약

}
