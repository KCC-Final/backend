package com.kcc.groo.bookshelf.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kys
 * @created 2025-10-14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookScrapRequest {
	
	private int bookshelfId;
	private String ISBN;

}
