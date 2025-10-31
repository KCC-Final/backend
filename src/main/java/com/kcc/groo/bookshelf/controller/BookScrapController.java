package com.kcc.groo.bookshelf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kcc.groo.bookshelf.data.dto.BookScrapDeleteRequest;
import com.kcc.groo.bookshelf.data.dto.BookScrapRequest;
import com.kcc.groo.bookshelf.data.dto.getBookScrapInfo;
import com.kcc.groo.bookshelf.data.model.BookScrap;
import com.kcc.groo.bookshelf.data.model.Bookshelf;
import com.kcc.groo.bookshelf.service.BookScrapService;
import com.kcc.groo.bookshelf.service.BookshelfService;
import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.jwt.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("api/v1/book/scrap")
public class BookScrapController {
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	@Autowired
	BookScrapService bookScrapService;
	@Autowired
	BookshelfService bookshelfService;
	
	/**
	 * @param bookScrapRequest
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-14
	 * 도서 스크랩 생성
	 */
	@PostMapping
	public ResponseEntity<CommonResponse<?>> createBookScrap (@RequestBody BookScrapRequest bookScrapRequest,
			HttpServletRequest request) {

		// get userId
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
		int result = bookScrapService.checkExistsBookByUserIdAndbookshelfId(userId, bookScrapRequest.getBookshelfId(), bookScrapRequest.getISBN());
		
		if(result > 0) {
			return ResponseEntity.badRequest()
					.body(new CommonResponse<>("this book is already exists", null));
		}
		
		BookScrap newBookScrap = bookScrapService.insertBookScrap(userId, bookScrapRequest);
		BookScrap getBookScrapInfo = bookScrapService.getBookScrap(userId,newBookScrap.getBookshelfId(), newBookScrap.getISBN());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>("createBookScrap success", getBookScrapInfo));
	}
	
	/**
	 * @param bookshelfId
	 * @param ISBN
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-15
	 * 도서 스크랩 단건 조회
	 */
	@GetMapping("/{bookshelfId}/{ISBN}")
	public ResponseEntity<CommonResponse<?>> getBookScrap (@PathVariable("bookshelfId") int bookshelfId, @PathVariable("ISBN") String ISBN, HttpServletRequest request) {

		// get userId
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
		BookScrap getBookScrap = bookScrapService.getBookScrap(userId, bookshelfId, ISBN);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new CommonResponse<>("getBookScrap information", getBookScrap));
	}
	
	/**
	 * @param bookshelfId
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-15
	 * 도서 스크랩 목록 조회
	 */
	@GetMapping("/list/{bookshelfId}")
	public ResponseEntity<CommonResponse<?>> getBookScrapList (@PathVariable("bookshelfId") int bookshelfId, HttpServletRequest request) {

		// get userId
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
		Bookshelf checkShelf = bookshelfService.getBookshelf(userId, bookshelfId);
		if (checkShelf != null) {
			List<BookScrap> getBookScrapInfoList = bookScrapService.getBookScrapList(userId, bookshelfId);
					return ResponseEntity.status(HttpStatus.CREATED)
							.body(new CommonResponse<>("Bookshelf and scrap list", getBookScrapInfoList));
		} else {
			return ResponseEntity.badRequest()
					.body(new CommonResponse<>("Cannot found Bookshelf", checkShelf));
		}
	}
	
	//delete
	/**
	 * @param getBookScrapInfo
	 * @param request
	 * @return
	 * 도서 스크랩 단건 삭제
	 */
	@DeleteMapping
	public ResponseEntity<CommonResponse<?>> deleteScrap (@RequestBody getBookScrapInfo getBookScrapInfo, HttpServletRequest request) {
		
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
		boolean success = bookScrapService.deleteBookScrap(userId, getBookScrapInfo.getBookshelfId(), getBookScrapInfo.getISBN());
		
        if (!success) {
        	return ResponseEntity.badRequest()
					.body(new CommonResponse<>("delete failed", getBookScrapInfo));
        }
        
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new CommonResponse<>("delete success", getBookScrapInfo));
	}
	
	/**
	 * @param bookshelfId
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-15
	 * 서재 별 도서 스크랩 된 도서 수 확인
	 */
	@GetMapping("/count/{bookshelfId}")
	public ResponseEntity<CommonResponse<?>> getBookScrapCount (@PathVariable("bookshelfId") int bookshelfId, HttpServletRequest request) {

		// get userId
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
		int countScrap = bookScrapService.countBookScrap(userId, bookshelfId);
		return ResponseEntity.ok(new CommonResponse<>("get Scrap Count, shelfId: "+bookshelfId , countScrap));
	} 
	
	/**
	 * @param bookshelfId
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-15
	 * 도서 스크랩 다중 삭제
	 */
	@DeleteMapping("/{bookshelfId}")
	public ResponseEntity<CommonResponse<?>> deleteSelectedBooks(@PathVariable("bookshelfId") int bookshelfId, @RequestBody BookScrapDeleteRequest bookScrapDeleteRequest,HttpServletRequest request) {

		// get userId
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
	    List<String> isbnList = bookScrapDeleteRequest.getIsbnList();

	    if (isbnList == null || isbnList.isEmpty()) {
	        return ResponseEntity.badRequest()
	            .body(new CommonResponse<>("Please select at least one book to delete", null));
	    }

	    int deletedCount = bookScrapService.deleteBookByIsbnList(userId, bookshelfId, isbnList);

	    return ResponseEntity.ok(
	        new CommonResponse<>("The selected books have been deleted successfully", deletedCount)
	    );
	}

}
