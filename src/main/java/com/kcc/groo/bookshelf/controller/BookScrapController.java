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

import com.kcc.groo.bookshelf.data.dto.BookScrapRequest;
import com.kcc.groo.bookshelf.data.model.BookScrap;
import com.kcc.groo.bookshelf.data.model.Bookshelf;
import com.kcc.groo.bookshelf.service.BookScrapService;
import com.kcc.groo.bookshelf.service.BookshelfService;
import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.jwt.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("api/v1/bookshelves")
public class BookScrapController {
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	@Autowired
	BookScrapService bookScrapService;
	@Autowired
	BookshelfService bookshelfService;
	
	@PostMapping
	public ResponseEntity<CommonResponse<?>> createBookScrap (@RequestBody BookScrapRequest bookScrapRequest,
			HttpServletRequest request) {

		// get userId
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
		int result = bookScrapService.checkExistsBookByUserIdAndbookshelfId(userId, bookScrapRequest.getBookshelfId(), bookScrapRequest.getISBN());
		
		if(result > 0) {
			return ResponseEntity.badRequest()
					.body(new CommonResponse<>("this book is already existsis bookshelf in th", null));
		}
		
		BookScrap newBookScrap = bookScrapService.insertBookScrap(userId, bookScrapRequest);
		BookScrap getBookScrapInfo = bookScrapService.getBookScrap(userId,newBookScrap.getBookshelfId(), newBookScrap.getISBN());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>("createBookScrap success", getBookScrapInfo));
	}
	
	//get
	@GetMapping
	public ResponseEntity<CommonResponse<?>> getBookScrap (@PathVariable("bookshelfId") int bookshelfId,String ISBN, HttpServletRequest request) {

		// get userId
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
		BookScrap getBookScrap = bookScrapService.getBookScrap(userId, bookshelfId, ISBN);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new CommonResponse<>("Follow information", getBookScrap));
	}
	
	//get list
	@GetMapping("/list")
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
					.body(new CommonResponse<>("Cannot found Bookshelf", null));
		}
	}
	
	//delete
	@DeleteMapping
	public ResponseEntity<CommonResponse<?>> deleteScrap (HttpServletRequest request,
			String ISBN, int bookshelfId) {
		
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
		boolean success = bookScrapService.deleteBookScrap(userId, bookshelfId, ISBN);
		
        if (!success) {
        	return ResponseEntity.badRequest()
					.body(new CommonResponse<>("delete failed", null));
        }
        
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new CommonResponse<>("delete success", null));
	}
	
	//get count
	@GetMapping("/count")
	public ResponseEntity<CommonResponse<?>> getBookScrapCount (@PathVariable("bookshelfId") int bookshelfId, HttpServletRequest request) {

		// get userId
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
		int countScrap = bookScrapService.countBookScrap(userId, bookshelfId);
		return ResponseEntity.ok(new CommonResponse<>("get Scrap Count", countScrap));
	} 

}
