package com.kcc.groo.bookshelf.service;

import java.util.List;

import com.kcc.groo.bookshelf.data.dto.BookshelfRequest;
import com.kcc.groo.bookshelf.data.model.Bookshelf;

public interface IBookshelfService {
	
	/**
	 * @param userId
	 * @param bookshelf
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 * 내 서재 생성
	 */
	Bookshelf insertBookshelf (String userId, BookshelfRequest bookshelfRequest);
	
	/**
	 * @param userId
	 * @param bookshelf
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 * 내 서재 수정
	 */
	Bookshelf updateBookshelf (String userId, int bookshelfId, BookshelfRequest bookshelfRequest);
	
	/**
	 * @param userId
	 * @param bookshelfId
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 * 내 서재 확인
	 */
	Bookshelf getBookshelf (String userId, int bookshelfId);
	
	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 * 내 서재 목록
	 */
	List<Bookshelf> getBookshelfList (String userId);
	
	/**
	 * @param userId
	 * @param bookshelfId
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 * 내 서재 삭제
	 */
	boolean deleteBookshelf (String userId, int bookshelfId);
	
	/**
	 * @param userId
	 * @param name
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 * 내 서재명 중복 확인
	 */
	int checkExistsBookshelfByUserId (String userId, String name);
	
	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 * 사용자의 서재명 목록
	 */
	List<String> BookshelfNameList (String userId);
	
	

}
