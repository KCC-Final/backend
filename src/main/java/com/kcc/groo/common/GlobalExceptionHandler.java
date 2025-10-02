package com.kcc.groo.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.review.exception.ReviewException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	 /**
	 * @author kys
	 * @since 2025-09-23
     * Validation 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<?>> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        log.error("[ValidationException] {}", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse<>(errorMessage, null));
    }

    /**
     * @author kys
	 * @since 2025-09-23
     * IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<?>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("[IllegalArgumentException] {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse<>(e.getMessage(), null));
    }

    /**
     * @author kys
	 * @since 2025-09-23
     * 인증/인가 관련 예외
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<CommonResponse<?>> handleSecurityException(SecurityException e) {
        log.error("[SecurityException] {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponse<>("Unauthorized", null));
    }

    /**
     * @author kys
	 * @since 2025-09-23
     * 그 외 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<?>> handleGeneralException(Exception e) {
        log.error("[Exception] {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponse<>("Internal server error", null));
    }
    
    /**
     * @author uyh
     * @since 2025-10-02
     * Review 도메인 비즈니스 예외 처리
     */
    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<CommonResponse<?>> handleReviewException(ReviewException e) {
        log.error("[ReviewException] ErrorCode: {}, Message: {}", 
                  e.getErrorCode().getCode(), e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(new CommonResponse<>(
                    e.getErrorCode().getCode() + " - " + e.getMessage(), 
                    null
                ));
    }

}
