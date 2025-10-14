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
	 */
	int createBookShelf (Bookshelf bookshelf);
	
	/**
	 * @param bookshelf
	 * @return
	 */
	int updateBookShelf (Bookshelf bookshelf);
	
	/**
	 * @param bookshelfId
	 * @param userId
	 * @return
	 */
	int deleteBookShelf (@Param("bookshelfId") int bookshelfId, @Param("userId") String userId);
	
	/**
	 * @param userId
	 * @param bookshelfId
	 * @return
	 */
	Bookshelf selectBookshelf (@Param("userId") String userId, @Param("bookshelfId") int bookshelfId);
	
	/**
	 * @param userId
	 * @return
	 */
	List<Bookshelf> BookshelfList (@Param("userId") String userId);
	
	/**
	 * @param userId
	 * @return
	 */
	int checkExistsBookshelfByUserId (@Param("userId") String userId, @Param("name") String name);
	
	/**
	 * @param userId
	 * @return
	 */
	List<String> BookshelfNameList (@Param("userId") String userId);
	
	int checkExistsBookshelfIdByUserId (@Param("userId") String userId, @Param("bookshelfId") int bookshelfId);

}
