package com.kcc.groo.bookshelf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kcc.groo.bookshelf.data.dto.BookshelfRequest;
import com.kcc.groo.bookshelf.data.model.Bookshelf;
import com.kcc.groo.bookshelf.service.BookshelfService;
import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.jwt.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/bookshelf")
public class BookshelfController {

	@Autowired
	BookshelfService bookshelfService;
	@Autowired
	JwtTokenProvider jwtTokenProvider;

	/**
	 * @param bookshelfRequest
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 */
	@PostMapping
	public ResponseEntity<CommonResponse<?>> createBookshelf(@RequestBody BookshelfRequest bookshelfRequest,
			HttpServletRequest request) {

		// get userId
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
		if(bookshelfService.checkExistsBookshelfByUserId(userId, bookshelfRequest.getName()) > 0) {
			return ResponseEntity.badRequest()
					.body(new CommonResponse<>("Bookshelf name already exists", null));
		}

		Bookshelf newBookshelf = bookshelfService.insertBookshelf(userId, bookshelfRequest);
		Bookshelf getBookInfo = bookshelfService.getBookshelf(userId, newBookshelf.getBookshelfId());
		return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>("createBookShelf success", getBookInfo));
	}

	/**
	 * @param bookshelfRequest
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 */
	@GetMapping("/{bookshelfId}")
	public ResponseEntity<CommonResponse<?>> getBookshelf(@PathVariable("bookshelfId") int bookshelfId,
			HttpServletRequest request) {

		// get userId
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
		Bookshelf getBookshelfInfo = bookshelfService.getBookshelf(userId, bookshelfId);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new CommonResponse<>("Bookshelf information", getBookshelfInfo));

	}
	
	/**
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 */
	@GetMapping ("/list")
	public ResponseEntity<CommonResponse<?>> getBookshelfList (HttpServletRequest request) {

		// get userId
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);

		List<Bookshelf> getBookshelfInfoList = bookshelfService.getBookshelfList(userId);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new CommonResponse<>("Bookshelf information", getBookshelfInfoList));

	}
	
	/**
	 * @param bookshelfId
	 * @param bookshelfRequest
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 */
	@PutMapping("/{bookshelfId}")
	public ResponseEntity<CommonResponse<?>> updateBookshelf (@PathVariable("bookshelfId") int bookshelfId, @RequestBody BookshelfRequest bookshelfRequest,
			HttpServletRequest request) {

		// get userId
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
		List<String> checkBookNames = bookshelfService.BookshelfNameList(userId);
		
		for (int i = 0; i < checkBookNames.size(); i++) {
			if (checkBookNames.get(i).equals(bookshelfRequest.getName())) {
				return ResponseEntity.badRequest()
						.body(new CommonResponse<>("Bookshelf name already exists", null));
			}
		}

		Bookshelf updateBookshelf = bookshelfService.updateBookshelf(userId, bookshelfId, bookshelfRequest);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new CommonResponse<>("Bookshelf update success", updateBookshelf));

	}
	
	/**
	 * @param bookshelfId
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-13
	 */
	@DeleteMapping("/{bookshelfId}")
	public ResponseEntity<CommonResponse<?>> deleteBookshelf (@PathVariable("bookshelfId") int bookshelfId, HttpServletRequest request) {

		// get userId
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);

		boolean deleteBookshelf = bookshelfService.deleteBookshelf(userId, bookshelfId);
		
		if (!deleteBookshelf) {
			return ResponseEntity.badRequest()
					.body(new CommonResponse<>("delete failed", null));
		}
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new CommonResponse<>("Bookshelf delete success", null));

	}
	

}
