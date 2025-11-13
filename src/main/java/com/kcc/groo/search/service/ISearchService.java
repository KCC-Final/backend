package com.kcc.groo.search.service;

import java.util.List;

import com.kcc.groo.review.data.dto.CommentResponse;
import com.kcc.groo.review.data.dto.ReviewResponse;
import com.kcc.groo.review.data.model.Review;
import com.kcc.groo.search.data.dto.SearchResult;
import com.kcc.groo.search.data.model.RefType;
import com.kcc.groo.user.data.model.Users;

public interface ISearchService {
	
	/**
	 * @param keyword
	 * @return
	 * @author kys
	 * @created 2025-10-22
	 * 사용자, 독후감, 댓글 통합 검색 - like 조회
	 */
	//List<SearchResult> searchAllByLike(String keyword);
	
	/**
	 * @param keyword
	 * @return
	 * @author kys
	 * @created 2025-11-13
	 * 사용자, 독후감, 댓글 통합 검색 - like + full text
	 */
	List<SearchResult> searchAll(String keyword);
	
	/**
	 * @param keyword
	 * @return
	 * @author kys
	 * @created 2025-11-13
	 * 사용자, 독후감, 댓글 통합 검색 - full-text 조회
	 */
	List<SearchResult> searchAllByFullText(String keyword);
	
	/**
	 * @param keyword
	 * @return
	 * @author kys
	 * @created 2025-11-13
	 * 검색 시 넘길 문자열 정제
	 */
	String sanitizeKeyword(String keyword);
	
	/**
	 * @param user
	 * @author kys
	 * @created 2025-11-13
	 */
	int insertUserIndex (Users user);
	
	/**
	 * @param review
	 * @author kys
	 * @created 2025-11-13
	 */
	
	int insertReviewIndex(ReviewResponse review);
	
	/**
	 * @param comment
	 * @author kys
	 * @created 2025-11-13
	 */
	int insertCommentIndex (CommentResponse comment);
	
	/**
	 * @param user
	 * @return
	 * @author kys
	 * @created 2025-11-13
	 */
	int updateUserIndex (Users user);
	
	/**
	 * @param review
	 * @return
	 * @author kys
	 * @created 2025-11-13
	 */
	int updateReviewIndex (ReviewResponse review);
	
	/**
	 * @param comment
	 * @return
	 * @author kys
	 * @created 2025-11-13
	 */
	int updateCommentIndex (CommentResponse comment);
	
	/**
	 * @param refType
	 * @param refId
	 * @return
	 * @author kys
	 * @created 2025-11-13
	 */
	int deleteIndex(RefType refType, String refId);
	

}
