package com.kcc.groo.search.service;

import java.util.List;

import com.kcc.groo.search.data.dto.SearchResult;

public interface ISearchService {
	
	/**
	 * @param keyword
	 * @return
	 * @author kys
	 * @created 2025-10-22
	 * 사용자, 독후감, 댓글 통합 검색
	 */
	List<SearchResult> searchAll(String keyword);

}
