package com.kcc.groo.search.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcc.groo.search.data.dto.SearchResult;
import com.kcc.groo.search.data.model.RefType;
import com.kcc.groo.search.data.model.SearchIndex;

@Mapper
@Repository
public interface ISearchRepository {
	
	/**
	 * @param keyword
	 * @return
	 * @author kys
	 * @created 2025-11-13
	 * 통합 검색 (user, review, comment) - full text
	 */
	List<SearchResult> searchAllByLike(@Param("keyword") String keyword);
	
	/**
	 * @param keyword
	 * @return
	 * @author kys
	 * @created 2025-11-13
	 * 통합 검색 (user, review, comment) - full text
	 */
	List<SearchResult> searchAllByFullText(@Param("keyword") String keyword);
	
	/**
	 * @param searchIndex
	 * @return
	 * @author kys
	 * @created 2025-11-13
	 * 통합 검색 (user, review, comment) 데이터 insert
	 */
	int insertSearchIndex(SearchIndex searchIndex);
	
	/**
	 * @param searchIndex
	 * @return
	 * @author kys
	 * @created 2025-11-13
	 * 통합 검색 (user, review, comment) 데이터 update
	 */
	int updateUserIndex(SearchIndex searchIndex);
	
	/**
	 * @param searchIndex
	 * @return
	 * @author kys
	 * @created 2025-11-13
	 * 통합 검색 (user, review, comment) 데이터 update
	 */
	int updateReviewIndex(SearchIndex searchIndex);
	
	/**
	 * @param searchIndex
	 * @return
	 * @author kys
	 * @created 2025-11-13
	 * 통합 검색 (user, review, comment) 데이터 update
	 */
	int updateCommentIndex(SearchIndex searchIndex);
	
	/**
	 * @param refType
	 * @param refId
	 * @author kys
	 * @created 2025-11-13
	 * 통합 검색 (user, review, comment) 데이터 delete
	 */
	int deleteIndex(@Param("refType") RefType refType, @Param("refId") String refId);
}
