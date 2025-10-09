package com.kcc.groo.quote.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kcc.groo.quote.service.QuoteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    @GetMapping("/today")
    public ResponseEntity<?> getTodayQuote() {
        Map<String, Object> todayQuote = quoteService.getTodayQuoteWithBook();
        if (todayQuote == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "can not found TodayQuote"));
        }
        return ResponseEntity.ok(todayQuote);
    }

}
