package com.kcc.groo.book.service;


import com.kcc.groo.book.dto.BookInfoResponseDTO;
import com.kcc.groo.bookshelf.dao.IBookScrapRepository;
import com.kcc.groo.review.dao.IReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 도서 정보 관련 비즈니스 로직을 처리하는 Service
 *
 * @author YunSung
 * @created 2025-11-07
 */
@RequiredArgsConstructor
@Service
public class BookService implements IBookService {

    private final IReviewRepository reviewRepository;

    private final IBookScrapRepository bookScrapRepository;

    @Override
    public BookInfoResponseDTO getBookInfoByIsbn(String isbn) {
        int reviewCount = reviewRepository.countReviewsByIsbn(isbn);
        int scrapCount = bookScrapRepository.getBookScrapCountByISBN(isbn);

        return BookInfoResponseDTO.builder()
                .reviewCount(reviewCount)
                .scrapCount(scrapCount).build();
    }

}
