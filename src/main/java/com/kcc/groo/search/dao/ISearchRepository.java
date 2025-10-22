package com.kcc.groo.search.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcc.groo.search.data.dto.SearchResult;

@Mapper
@Repository
public interface ISearchRepository {

	/**
	 * @param keyword
	 * @return
	 * @author kys
	 * @created 2025-10-22
	 * 통합 검색 (user, review, comment)
	 */
	List<SearchResult> searchAll(@Param("keyword") String keyword);
	
}
