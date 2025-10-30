package com.kcc.groo.bookshelf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kcc.groo.challenge.service.IChallengeService; // 추가
import com.kcc.groo.bookshelf.dao.IBookScrapRepository;
import com.kcc.groo.bookshelf.dao.IBookshelfRepository;
import com.kcc.groo.bookshelf.data.dto.BookScrapRequest;
import com.kcc.groo.bookshelf.data.model.BookScrap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookScrapService implements IBookScrapService {

    @Autowired
    IBookScrapRepository bookScrapRepository;
    @Autowired
    IBookshelfRepository bookshelfRepository;
    @Autowired
    IChallengeService challengeService; // 추가

    @Override
    public BookScrap insertBookScrap(String userId, BookScrapRequest bookScrapRequest) {
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("can not found account");
        }
        if (!StringUtils.hasText(bookScrapRequest.getISBN())) {
            throw new IllegalArgumentException("can not found ISBN");
        }
        if (bookScrapRequest.getBookshelfId() <= 0
                || bookshelfRepository.checkExistsBookshelfIdByUserId(userId, bookScrapRequest.getBookshelfId()) <= 0) {
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

        // 첫 발견 뱃지 자동 검사
        try {
            challengeService.checkAndAwardBadges(userId);
            log.info(" BookScrap inserted, badge check triggered for userId={}", userId);
        } catch (Exception e) {
            log.error(" Failed to check badge after book scrap: userId={}, error={}", userId, e.getMessage());
        }

        BookScrap createBookScrapInfo = bookScrapRepository.selectBookScrap(
                userId, bookScrapRequest.getISBN(), bookScrapRequest.getBookshelfId()
        );
        return createBookScrapInfo;
    }

    @Override
    public BookScrap getBookScrap(String userId, int bookshelfId, String ISBN) {
        BookScrap bookScrapInfo = bookScrapRepository.selectBookScrap(userId, ISBN, bookshelfId);
        if (bookScrapInfo != null) {
            return bookScrapInfo;
        } else {
            throw new IllegalArgumentException("can not found bookScrapInfo");
        }
    }

    @Override
    public List<BookScrap> getBookScrapList(String userId, int bookshelfId) {
        return bookScrapRepository.BookScrapList(userId, bookshelfId);
    }

    @Override
    public boolean deleteBookScrap(String userId, int bookshelfId, String ISBN) {
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
        return bookScrapRepository.checkExistsBookByUserIdAndbookshelfId(userId, ISBN, bookshelfId);
    }

    @Override
    public int countBookScrap(String userId, int bookshelfId) {
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("cannot found user");
        }
        if (bookshelfId <= 0) {
            throw new IllegalArgumentException("cannot found bookshelfId");
        }
        return bookScrapRepository.countBookScrap(userId, bookshelfId);
    }

    @Override
    public int deleteBookByIsbnList(String userId, int bookshelfId, List<String> isbnList) {
        return bookScrapRepository.deleteBooksByIsbnList(userId, bookshelfId, isbnList);
    }
}
