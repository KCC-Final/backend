package com.kcc.groo.bookshelf.data.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kys
 * @created 2025-10-13
 * scrap VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookScrap {
	
	private int bookshelfId;
    @JsonProperty
	private String ISBN;
	private LocalDateTime createdAt;
	private String userId;

}
