package com.kcc.groo.quote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.quote.data.dto.TodayQuoteDto;
import com.kcc.groo.quote.service.QuoteService;

@RestController
@RequestMapping("/api/v1/quotes")
public class QuoteController {

	@Autowired
    private QuoteService quoteService;

    /**
     * @return
	 * @author kys
	 * @created 2025-10-10
	 * 오늘의 명언 조회 api
     */
    @GetMapping("/daily")
    public ResponseEntity<CommonResponse<?>> getTodayQuote() {
    	TodayQuoteDto todayQuote = quoteService.getTodayQuote();
        if (todayQuote == null) {
            return ResponseEntity.badRequest().body(new CommonResponse<>("fail to get today quote", null));
        }
        return ResponseEntity.ok(new CommonResponse<>("success to get today quote", todayQuote));
    }
}
