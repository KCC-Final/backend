package com.kcc.groo.quote.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kcc.groo.quote.dao.IQuoteRepository;
import com.kcc.groo.quote.data.dto.BookInfoDto;
import com.kcc.groo.quote.data.model.Quotes;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuoteService implements IQuoteService {
	
	@Autowired
	IQuoteRepository quoteRepository;
	@Autowired
	AladinBookService aladinBookService;

	@Override
	@Scheduled(cron = "0 0 0 * * *")
	public void updateTodayQuote() {
		log.info("updateTodayQuote");

        Quotes nextQuote = quoteRepository.getNextQuote();

        if (nextQuote == null) {
            log.info("reselect TodayQuote");
            quoteRepository.resetAllSelectedDates();
            nextQuote = quoteRepository.getNextQuote();
        }

        if (nextQuote != null) {
            quoteRepository.updateSelectedDate(nextQuote.getSentenceId(), LocalDate.now());
            log.info("TodayQuote 문장: {}", nextQuote.getSentenceContent());
        }
    }

    public Map<String, Object> getTodayQuoteWithBook() {
        Quotes today = quoteRepository.getTodayQuote();
        if (today == null) return null;

        BookInfoDto book = aladinBookService.getBookByIsbn(today.getISBN());

        Map<String, Object> result = new HashMap<>();
        result.put("sentence", today);
        result.put("book", book);

        return result;
    }

}
