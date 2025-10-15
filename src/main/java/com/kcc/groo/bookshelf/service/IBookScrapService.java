package com.kcc.groo.bookshelf.service;

import java.util.List;

import com.kcc.groo.bookshelf.data.dto.BookScrapRequest;
import com.kcc.groo.bookshelf.data.model.BookScrap;

public interface IBookScrapService {
	
	/**
	 * @param userId
	 * @param bookScrapRequest
	 * @return
	 * @author kys
	 * @created 2025-10-14
	 * 도서 스크랩 생성
	 */
	BookScrap insertBookScrap (String userId, BookScrapRequest bookScrapRequest);
	
	/**
	 * @param userId
	 * @param bookshelfId
	 * @return
	 * @author kys
	 * @created 2025-10-14
	 * 도서 스크랩 단건 조회
	 */
	BookScrap getBookScrap (String userId, int bookshelfId, String ISBN);
	
	/**
	 * @param userId
	 * @param bookshelfId
	 * @return
	 * @author kys
	 * @created 2025-10-14
	 * 도서 스크랩 목록 조회
	 */
	List <BookScrap> getBookScrapList (String userId, int bookshelfId);
	
	/**
	 * @param userId
	 * @param bookshelfId
	 * @param ISBN
	 * @return
	 * @author kys
	 * @created 2025-10-14
	 * 스크랩한 도서 삭제
	 */
	boolean deleteBookScrap (String userId, int bookshelfId, String ISBN);
	
	/**
	 * @param userId
	 * @param bookshelfId
	 * @param ISBN
	 * @return
	 * @author kys
	 * @created 2025-10-14
	 * 스크랩 할 도서가 기존에 존재하는지 확인
	 */
	int checkExistsBookByUserIdAndbookshelfId (String userId, int bookshelfId, String ISBN);
	
	/**
	 * @param userId
	 * @param bookshelfId
	 * @return
	 * @author kys
	 * @created 2025-10-14
	 * 스크랩된 도서 수 확인
	 */
	int countBookScrap (String userId, int bookshelfId);
	
	/**
	 * @param userId
	 * @param bookshelfId
	 * @param isbnList
	 * @return
	 * @author kys
	 * @created 2025-10-15
	 * 선택된 도서 삭제
	 */
	int deleteBookByIsbnList (String userId, int bookshelfId, List<String> isbnList);

}
