package com.kcc.groo.quote.data.dto;

import java.time.LocalDateTime;

import com.kcc.groo.quote.data.model.Quotes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteDto {
	
	private int sentenceId;
	private String sentenceContent;
	private String ISBN;
	private LocalDateTime selectedDate;

}