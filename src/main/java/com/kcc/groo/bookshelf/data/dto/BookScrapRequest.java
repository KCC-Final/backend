package com.kcc.groo.bookshelf.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookScrapRequest {
	
	private int bookshelfId;
	private String ISBN;

}
