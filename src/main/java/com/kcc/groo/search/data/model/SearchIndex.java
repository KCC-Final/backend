package com.kcc.groo.search.data.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchIndex {
	
 private Long id;
 private RefType refType;
 private String refId;
 private String title;
 private String subtext;
 private String content;
 private LocalDateTime createdAt;

}


