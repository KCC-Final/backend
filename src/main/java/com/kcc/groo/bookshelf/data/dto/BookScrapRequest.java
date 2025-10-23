package com.kcc.groo.bookshelf.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("ISBN")  // JSON의 "ISBN"을 이 필드에 매핑
    private String ISBN;

}
