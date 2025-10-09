package com.kcc.groo.quote.data.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quotes {
	
	private int sentenceId;
	private String sentenceContent;
	private String ISBN;
	private LocalDateTime selectedDate;

}
