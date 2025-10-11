package com.kcc.groo.quote.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodayQuoteDto { //스케쥴링용
	private QuoteDto sentence;
	private BookInfoDto book;
}