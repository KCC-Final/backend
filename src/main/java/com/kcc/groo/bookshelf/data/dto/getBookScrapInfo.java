package com.kcc.groo.bookshelf.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kys
 * @created 2025-10-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class getBookScrapInfo {
	
	private int bookshelfId;
	private String ISBN;

}
