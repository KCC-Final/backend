package com.kcc.groo.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kcc.groo.common.dto.CommonResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler
	public CommonResponse<String> exceptionHandler (RuntimeException e) {
		log.error("[exceptionHandler] ex", e);
		return new CommonResponse<String>("Exception", e.getMessage());
	}

}
