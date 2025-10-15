package com.kcc.groo.bookshelf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.kcc.groo.bookshelf.data.model.Bookshelf;

import org.apache.ibatis.annotations.Param;

@Repository
@Mapper
public interface IBookshelfRepository {
	
	/**
	 * @param bookshelf
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 * 책장 생성
	 */
	int createBookShelf (Bookshelf bookshelf);
	
	/**
	 * @param bookshelf
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 * 책장 수정
	 */
	int updateBookShelf (Bookshelf bookshelf);
	
	/**
	 * @param bookshelfId
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 * 책장 삭제
	 */
	int deleteBookShelf (@Param("bookshelfId") int bookshelfId, @Param("userId") String userId);
	
	/**
	 * @param userId
	 * @param bookshelfId
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 * 책장 상세
	 */
	Bookshelf selectBookshelf (@Param("userId") String userId, @Param("bookshelfId") int bookshelfId);
	
	/**
	 * @param userId
	 * @return
	 * 책장 목록
	 */
	List<Bookshelf> BookshelfList (@Param("userId") String userId);
	
	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 * 사용자 아이디를 이용해 이미 존재하는 책장 이름인지 확인
	 */
	int checkExistsBookshelfByUserId (@Param("userId") String userId, @Param("name") String name);
	
	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 * 책장명 목록
	 */
	List<String> BookshelfNameList (@Param("userId") String userId);
	
	/**
	 * @param userId
	 * @param bookshelfId
	 * @return
	 * @author kys
	 * @created 2025-10-14
	 * 사용자 아이디와 책장 아이디를 이용해 이미 존재하는 책장인지 확인
	 */
	int checkExistsBookshelfIdByUserId (@Param("userId") String userId, @Param("bookshelfId") int bookshelfId);

}
