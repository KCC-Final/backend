package com.kcc.groo.bookshelf.data.dto;

import java.time.LocalDateTime;

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
public class BookScrapResponse {
	
	private int bookshelfId;
	private String name;
	private String ISBN;
	private String userId;
	private LocalDateTime createdAt;

}
