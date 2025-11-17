package com.kcc.groo.search.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcc.groo.review.data.dto.CommentResponse;
import com.kcc.groo.review.data.dto.ReviewResponse;
import com.kcc.groo.review.data.model.Review;
import com.kcc.groo.search.dao.ISearchRepository;
import com.kcc.groo.search.data.dto.SearchResult;
import com.kcc.groo.search.data.model.RefType;
import com.kcc.groo.search.data.model.SearchIndex;
import com.kcc.groo.user.data.model.Users;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SearchService implements ISearchService{
	
	@Autowired
	ISearchRepository searchRepository;
	
	public List<SearchResult> searchAll(String keyword) {
	    String safe = sanitizeKeyword(keyword) + "*";

	    List<SearchResult> ft = searchRepository.searchAllByFullText(safe);

	    // fulltext 결과가 없으면 LIKE 검색 수행
	    if (ft.isEmpty()) {
	        List<SearchResult> like = searchRepository.searchAllByLike(keyword);
	        return like;
	    }

	    return ft;
	}

	
	public List<SearchResult> searchAllByFullText(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("insert keyword");
        }
        
        String safeKeyword = sanitizeKeyword(keyword)+"*";
        return searchRepository.searchAllByFullText(safeKeyword);
    }
	
	public String sanitizeKeyword(String keyword) {
	    return keyword.trim();
	}

	 @Override
	    public int insertUserIndex(Users user) {
	        SearchIndex index = SearchIndex.builder()
	                .refType(RefType.USER)
	                .refId(user.getUserId())  // String
	                .title(user.getName()+ " | "+ user.getUserId() + " | " + user.getNickname())
	                .subtext(user.getUserId() + " | " + user.getNickname() + " | " + user.getEmail())
	                .content(user.getIntroduction())
	                .build();
	        int result = searchRepository.insertSearchIndex(index);
	        log.info(">>> inserted search_index count = {}", result);
	        return result;

	    }

	    @Override
	    public int updateUserIndex(Users user) {
	    	
	        SearchIndex index = SearchIndex.builder()
	        		.refType(RefType.USER)
	                .refId(user.getUserId())
	                .title(user.getName()+ " | "+ user.getUserId() + " | " + user.getNickname())
	                .subtext(user.getUserId() + " | " + user.getNickname() + " | " + user.getEmail())
	                .content(user.getIntroduction())
	                .build();

	        return searchRepository.updateUserIndex(index);
	    }

	    @Override
	    public int insertReviewIndex(ReviewResponse review) {
	        SearchIndex index = SearchIndex.builder()
	                .refType(RefType.REVIEW)
	                .refId(String.valueOf(review.getReviewId()))
	                .title(review.getReviewTitle())
	                .subtext(review.getUserId() + " | " + review.getIsbn() + " | " + review.getCategory())
	                .content(review.getReviewContent())
	                .build();

	        return searchRepository.insertSearchIndex(index);
	    }

	    @Override
	    public int updateReviewIndex(ReviewResponse review) {
	        SearchIndex index = SearchIndex.builder()
	                .refId(String.valueOf(review.getReviewId()))
	                .title(review.getReviewTitle())
	                .subtext(review.getUserId() + " | " + review.getIsbn() + " | " + review.getCategory())
	                .content(review.getReviewContent())
	                .build();

	        return searchRepository.updateReviewIndex(index);
	    }


	    @Override
	    public int insertCommentIndex(CommentResponse comment) {

	        String truncatedTitle = comment.getContent().length() > 255
	                ? comment.getContent().substring(0, 255)
	                : comment.getContent();

	        SearchIndex index = SearchIndex.builder()
	                .refType(RefType.COMMENT)
	                .refId(String.valueOf(comment.getCommentId()))
	                .title(truncatedTitle)
	                .subtext(comment.getAuthorNickname() + " | " + comment.getReviewTitle())
	                .content(comment.getContent())
	                .build();

	        return searchRepository.insertSearchIndex(index);
	    }

	    @Override
	    public int updateCommentIndex(CommentResponse comment) {

	        String truncatedTitle = comment.getContent().length() > 255
	                ? comment.getContent().substring(0, 255)
	                : comment.getContent();

	        SearchIndex index = SearchIndex.builder()
	                .refId(String.valueOf(comment.getCommentId()))
	                .title(truncatedTitle)
	                .subtext(comment.getAuthorNickname() + " | " + comment.getReviewTitle())
	                .content(comment.getContent())
	                .build();

	        return searchRepository.updateCommentIndex(index);
	    }


	    @Override
	    public int deleteIndex(RefType refType, String refId) {
	        return searchRepository.deleteIndex(refType, refId);
	    }

	}


