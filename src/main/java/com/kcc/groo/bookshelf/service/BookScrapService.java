package com.kcc.groo.bookshelf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kcc.groo.bookshelf.dao.IBookScrapRepository;
import com.kcc.groo.bookshelf.dao.IBookshelfRepository;
import com.kcc.groo.bookshelf.data.dto.BookScrapRequest;
import com.kcc.groo.bookshelf.data.model.BookScrap;

@Service
public class BookScrapService implements IBookScrapService {
	
	@Autowired
	IBookScrapRepository bookScrapRepository;
	@Autowired
	IBookshelfRepository bookshelfRepository;

	@Override
	public BookScrap insertBookScrap(String userId, BookScrapRequest bookScrapRequest) {
		// TODO Auto-generated method stub
		if (!StringUtils.hasText(userId)) {
			throw new IllegalArgumentException("can not found account");
		}
		if (!StringUtils.hasText(bookScrapRequest.getISBN())) {
			throw new IllegalArgumentException("can not found ISBN");
		}
		if (bookScrapRequest.getBookshelfId() <= 0 || bookshelfRepository.checkExistsBookshelfIdByUserId(userId, bookScrapRequest.getBookshelfId()) <= 0) {
			throw new IllegalArgumentException("Bookshelf does not exists");
		}
		
		BookScrap newBookScrap = new BookScrap();
		newBookScrap.setUserId(userId);
		newBookScrap.setBookshelfId(bookScrapRequest.getBookshelfId());
		newBookScrap.setISBN(bookScrapRequest.getISBN());
		
		int result = bookScrapRepository.createBookScrap(newBookScrap);
		
		if (result <= 0) {
			throw new RuntimeException("failed create bookScrap");
		}
		
		BookScrap createBookScrapInfo = bookScrapRepository.selectBookScrap(userId, bookScrapRequest.getISBN(), bookScrapRequest.getBookshelfId());
		return createBookScrapInfo;
	}

	@Override
	public BookScrap getBookScrap(String userId, int bookshelfId, String ISBN) {
		// TODO Auto-generated method stub
		BookScrap bookScrapInfo = bookScrapRepository.selectBookScrap(userId, ISBN, bookshelfId);
		if(bookScrapInfo != null) {
			return bookScrapInfo;
		} else {
			throw new IllegalArgumentException("can not found bookScrapInfo");
		}
	}

	@Override
	public List<BookScrap> getBookScrapList(String userId, int bookshelfId) {
		// TODO Auto-generated method stub
		return bookScrapRepository.BookScrapList(userId, bookshelfId);
	}

	@Override
	public boolean deleteBookScrap(String userId, int bookshelfId, String ISBN) {
		// TODO Auto-generated method stub
		if (!StringUtils.hasText(userId)) {
			throw new IllegalArgumentException("cannot found logged in user");
		}
		if (!StringUtils.hasText(ISBN)) {
			throw new IllegalArgumentException("cannot found ISBN");
		}
		if (bookshelfId <= 0) {
			throw new IllegalArgumentException("cannot found bookshelfId");
		}
		
		int deleted = bookScrapRepository.deleteBookScrap(userId, ISBN);
		return deleted > 0;
	}

	@Override
	public int checkExistsBookByUserIdAndbookshelfId(String userId, int bookshelfId, String ISBN) {
		// TODO Auto-generated method stub
		return bookScrapRepository.checkExistsBookByUserIdAndbookshelfId(userId, ISBN, bookshelfId);
	}

	@Override
	public int countBookScrap(String userId, int bookshelfId) {
		// TODO Auto-generated method stub
		if (!StringUtils.hasText(userId)) {
			throw new IllegalArgumentException("cannot found user");
		}
		if (bookshelfId <= 0) {
			throw new IllegalArgumentException("cannot found bookshelfId");
		}
		return bookScrapRepository.countBookScrap(userId, bookshelfId);
	}

}
