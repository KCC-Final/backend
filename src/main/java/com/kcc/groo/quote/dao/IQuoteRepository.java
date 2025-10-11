package com.kcc.groo.quote.dao;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcc.groo.quote.data.model.Quotes;

@Repository
@Mapper
public interface IQuoteRepository {
	
	/**
	 * @return
	 * @author kys
	 * @created 2025-10-10
	 * 오늘 선택된 문장 조회
	 */

	Quotes getTodayQuote();

    /**
     * @param sentenceId
     * @param selectedDate
     * @return
	 * @author kys
	 * @created 2025-10-10
	 * 특정 문장 선택일 업데이트
     */
    int updateSelectedDate(@Param("sentenceId") int sentenceId,
                           @Param("selectedDate") LocalDate selectedDate);

    /**
     * @return
	 * @author kys
	 * @created 2025-10-10
	 * 아직 선택되지 않은 다음 명언 조회
     */
    Quotes getNextQuote();

    /**
     * @return
	 * @author kys
	 * @created 2025-10-10
	 * 모든 명언 선택일 초기화 (db 내 모든 명언이 선택되었을 경우)
     */
    int resetAllSelectedDates();

	/**
	 * @return
	 * @author kys
	 * @created 2025-10-10
	 * db 명언 리스트 형태로 반환
	 */
	List<Quotes> getTodayQuoteList();


}
