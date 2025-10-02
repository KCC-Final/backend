package com.kcc.groo.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResponse<T> { //공통 응답 객체
	private String message; //message
	private T data; //data
}
