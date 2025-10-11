package com.kcc.groo.quote.service;

import com.kcc.groo.quote.data.dto.TodayQuoteDto;

public interface IQuoteService {
	
	/**
	 * @author kys
	 * @created 2025-10-10
	 * 매일 자정마다 오늘의 문장을 갱신
	 */
	void updateTodayQuote();
	
	/**
	 * @author kys
	 * @created 2025-10-10
	 * 사용자가 요청한 오늘의 명언 dto로 반환 및 db에서 조회
	 */
	TodayQuoteDto getTodayQuote();

}
