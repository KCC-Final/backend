package com.kcc.groo.bookshelf.data.dto;

import java.util.List;

import lombok.Data;

/**
 * @author kys
 * @created 2025-10-15
 */
@Data
public class BookScrapDeleteRequest {
	private List<String> isbnList;
}
