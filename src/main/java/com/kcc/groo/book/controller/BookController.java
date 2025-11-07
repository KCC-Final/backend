package com.kcc.groo.book.controller;

import com.kcc.groo.book.dto.BookInfoResponseDTO;
import com.kcc.groo.book.service.IBookService;
import com.kcc.groo.common.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 도서 관련 API 요청을 처리하는 컨트롤러
 *
 * @author YunSung
 * @created 2025-11-07
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "도서 정보 조회 API")
public class BookController {

    private final IBookService bookService;

    /**
     * 특정 도서의 독후감 수와 스크랩 수 조회
     *
     * @param isbn 도서 ISBN
     * @return 도서 정보 응답 DTO
     * @author YunSung
     * @created 2025-11-07
     */
    @Operation(summary = "특정 도서의 독후감 수와 스크랩 수 조회")
    @GetMapping("/{isbn}")
    public ResponseEntity<CommonResponse<BookInfoResponseDTO>> getBookInfoByIsbn(@PathVariable String isbn) {
        BookInfoResponseDTO bookInfo = bookService.getBookInfoByIsbn(isbn);
        return ResponseEntity.ok(new CommonResponse<>("Book info retrieved successfully", bookInfo));
    }
}
