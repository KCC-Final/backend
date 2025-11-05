package com.kcc.groo.quote.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.kcc.groo.quote.dao.IQuoteRepository;
import com.kcc.groo.quote.data.dto.BookInfoDto;
import com.kcc.groo.quote.data.dto.QuoteDto;
import com.kcc.groo.quote.data.dto.TodayQuoteDto;
import com.kcc.groo.quote.data.model.Quotes;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuoteService implements IQuoteService {

	@Autowired
	IQuoteRepository quoteRepository;
	@Autowired
	AladinBookService aladinBookService;

	private TodayQuoteDto todayQuoteDto; // 스케줄링 정보 저장용

	@Scheduled(cron = "0 0 0 * * *") // 자정마다 캐시 초기화
	@Override
	public void updateTodayQuote() {
		Quotes quotes = quoteRepository.getNextQuote();
		if (quotes == null) {
			quoteRepository.resetAllSelectedDates();
			quotes = quoteRepository.getNextQuote();
		}
		if (quotes != null) {// DTO로 변환
			QuoteDto nextSentence = new QuoteDto();
			nextSentence.setSentenceId(quotes.getSentenceId());
			nextSentence.setSentenceContent(quotes.getSentenceContent());
			nextSentence.setISBN(quotes.getISBN());
			nextSentence.setSelectedDate(quotes.getSelectedDate());

			// selected_date 업데이트
			quoteRepository.updateSelectedDate(nextSentence.getSentenceId(), LocalDate.now());

			// 도서 정보 가져오기
			BookInfoDto book = aladinBookService.getBookByIsbn(nextSentence.getISBN());

			// 저장
			todayQuoteDto = new TodayQuoteDto(nextSentence, book);
		} else {
			todayQuoteDto = null;
		}
	}

	public TodayQuoteDto getTodayQuote() {

		LocalDate today = LocalDate.now();

		if (todayQuoteDto == null ||
				todayQuoteDto.getSentence() == null
	            || todayQuoteDto.getSentence().getSelectedDate() == null
	            || !todayQuoteDto.getSentence().getSelectedDate().equals(today)) {
			Quotes quotes = quoteRepository.getTodayQuote();

	        if (quotes != null) {
	            QuoteDto sentence = new QuoteDto();
	            sentence.setSentenceId(quotes.getSentenceId());
	            sentence.setSentenceContent(quotes.getSentenceContent());
	            sentence.setISBN(quotes.getISBN());
	            sentence.setSelectedDate(quotes.getSelectedDate());

	            BookInfoDto book = aladinBookService.getBookByIsbn(sentence.getISBN());
	            todayQuoteDto = new TodayQuoteDto(sentence, book);

	        } else {
	            todayQuoteDto = null;
	        }
	    }

	    return todayQuoteDto;
	}

}