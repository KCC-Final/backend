package com.kcc.groo.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.search.data.dto.SearchResult;
import com.kcc.groo.search.service.ISearchService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
	
	@Autowired
	ISearchService searchService;
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	/**
	 * @param keyword
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-22
	 * 통합검색
	 */
//	@GetMapping()
//    public ResponseEntity<CommonResponse<?>> searchAll(@RequestParam("q") String keyword, HttpServletRequest request) {
//
//        String accessToken = jwtTokenProvider.resolveAccessToken(request);
//        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new CommonResponse<>("Need to login", null));
//        }
//
//        String userId = jwtTokenProvider.getUserId(accessToken);
//
//        List<SearchResult> results = searchService.searchAllByLike(keyword);
//
//        return ResponseEntity.ok()
//                .body(new CommonResponse<>("search results for user: " + userId, results));
//    }
	
	/**
	 * @param keyword
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-22
	 * 통합검색
	 */
	@GetMapping
    public ResponseEntity<CommonResponse<?>> searchAllByFullText(@RequestParam("q") String keyword, HttpServletRequest request) {

        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new CommonResponse<>("Need to login", null));
        }

        String userId = jwtTokenProvider.getUserId(accessToken);

        List<SearchResult> results = searchService.searchAll(keyword);

        return ResponseEntity.ok()
                .body(new CommonResponse<>("search results for user: " + userId, results));
    }
}

