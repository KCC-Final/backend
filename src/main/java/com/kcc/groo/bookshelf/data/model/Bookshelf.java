package com.kcc.groo.bookshelf.data.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kys
 * @created 2025-10-13
 * Bookshelf VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bookshelf {
	
	private int bookshelfId;
	private String name;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String userId;
	

}
