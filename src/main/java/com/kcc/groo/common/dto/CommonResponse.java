package com.kcc.groo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

//공통 응답 객체
@Data
@AllArgsConstructor
public class CommonResponse<T> {
	private String message; //message
	private T data; //data
}
