package com.kcc.groo.bookshelf.data.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author kys
 * @created 2025-10-15
 */
@Data
public class BookScrapDeleteRequest {

    @JsonProperty
	private List<String> isbnList;
}
