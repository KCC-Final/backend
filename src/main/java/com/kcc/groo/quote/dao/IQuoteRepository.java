package com.kcc.groo.quote.dao;

import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcc.groo.quote.data.model.Quotes;

@Repository
@Mapper
public interface IQuoteRepository {
	
	Quotes getTodayQuote();

    int updateSelectedDate(@Param("sentenceId") int sentenceId,
                           @Param("selectedDate") LocalDate selectedDate);

    Quotes getNextQuote();

    int resetAllSelectedDates();

}
