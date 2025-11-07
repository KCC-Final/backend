package com.kcc.groo.book.service;

import com.kcc.groo.book.dto.BookInfoResponseDTO;

/**
 * 도서 정보 관련 비즈니스 로직을 처리하는 Service 인터페이스
 *
 * @author YunSung
 * @created 2025-11-07
 */
public interface IBookService {

    /**
     * ISBN으로 도서 정보 조회 (작성된 독후감 수, 스크랩 수)
     *
     * @param isbn 도서의 ISBN
     * @author YunSung
     * @created 2025-11-07
     */
    BookInfoResponseDTO getBookInfoByIsbn(String isbn);
}
