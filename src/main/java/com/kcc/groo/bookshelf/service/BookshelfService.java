package com.kcc.groo.bookshelf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kcc.groo.bookshelf.dao.IBookshelfRepository;
import com.kcc.groo.bookshelf.data.dto.BookshelfRequest;
import com.kcc.groo.bookshelf.data.model.Bookshelf;
import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.user.dao.IUsersRepository;

@Service
public class BookshelfService implements IBookshelfService {

	@Autowired
	IBookshelfRepository bookshelfRepository;
	@Autowired
	IUsersRepository usersRepository;

	@Override
	public Bookshelf insertBookshelf(String userId, BookshelfRequest bookshelfRequest) {
		// TODO Auto-generated method stub
		if (!StringUtils.hasText(userId)) {
			throw new IllegalArgumentException("can not found account");
		}

		Bookshelf newBookshelf = new Bookshelf();
		newBookshelf.setUserId(userId);
		newBookshelf.setName(bookshelfRequest.getName());

		int result = bookshelfRepository.createBookShelf(newBookshelf);

		if (result <= 0) {
			throw new RuntimeException("failed create bookshelf");
		}

		Bookshelf createdBookshelfInfo = bookshelfRepository.selectBookshelf(userId, newBookshelf.getBookshelfId());
		return createdBookshelfInfo;
	}

	@Override
	public Bookshelf updateBookshelf(String userId, int bookshelfId, BookshelfRequest bookshelfRequest) {
		// TODO Auto-generated method stub
		if (!StringUtils.hasText(userId)) {
			throw new IllegalArgumentException("can not found account");
		}
		
		List<String> checkBookNames = bookshelfRepository.BookshelfNameList(userId);
		
		for (int i = 0; i < checkBookNames.size(); i++) {
			if (checkBookNames.get(i).equals(bookshelfRequest.getName())) {
				throw new IllegalArgumentException("Bookshelf name already exists: " + bookshelfId+" : "+checkBookNames.get(i));
			}
		}

		// set Bookshelf
		Bookshelf updateBookshelf = bookshelfRepository.selectBookshelf(userId, bookshelfId);

		if (updateBookshelf == null) {
			throw new IllegalArgumentException("Bookshelf not found for id: " + bookshelfId);
		}

		if (bookshelfRepository.checkExistsBookshelfByUserId(userId, bookshelfRequest.getName()) <= 0) {

			if (StringUtils.hasText(bookshelfRequest.getName())) {
				updateBookshelf.setName(bookshelfRequest.getName());
			}
		}

		int result = bookshelfRepository.updateBookShelf(updateBookshelf);

		if (result > 0) {
			return bookshelfRepository.selectBookshelf(userId, bookshelfId);
		} else {
			throw new RuntimeException("failed update Bookshelf information");
		}
	}

	@Override
	public Bookshelf getBookshelf(String userId, int bookshelfId) {
		Bookshelf BookshelfInfo = bookshelfRepository.selectBookshelf(userId, bookshelfId);
		if (BookshelfInfo != null) {
			return BookshelfInfo;
		} else {
			throw new IllegalArgumentException("can not found BookshelfInfo");
		}
	}

	@Override
	public List<Bookshelf> getBookshelfList(String userId) {
		// TODO Auto-generated method stub
		return bookshelfRepository.BookshelfList(userId);
	}

	@Override
	public boolean deleteBookshelf(String userId, int bookshelfId) {
		// TODO Auto-generated method stub
		if (!StringUtils.hasText(userId)) {
			throw new IllegalArgumentException("cannot found logged in user");
		}
		if (bookshelfId <= 0) {
			throw new IllegalArgumentException("cannot found bookshelfId");
		}

		int deleted = bookshelfRepository.deleteBookShelf(bookshelfId, userId);
		return deleted > 0;
	}

	@Override
	public int checkExistsBookshelfByUserId(String userId, String name) {
		// TODO Auto-generated method stub
		return bookshelfRepository.checkExistsBookshelfByUserId(userId, name);
	}

	@Override
	public List<String> BookshelfNameList(String userId) {
		// TODO Auto-generated method stub
		return bookshelfRepository.BookshelfNameList(userId);
	}

}
