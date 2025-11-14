package com.kcc.groo.common.exception;

import com.kcc.groo.common.dto.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Validation 실패 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldError().getDefaultMessage();
        log.error("[ValidationException] {}", errorMessage);

        if (isSseRequest(request)) {
            return toSseError(errorMessage, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse<>(errorMessage, null));
    }

    /**
     * QueryParam, PathVariable Validation 실패 처리
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String errorMessage = e.getConstraintViolations().iterator().next().getMessage();
        log.error("[ConstraintViolationException] {}", errorMessage);

        if (isSseRequest(request)) {
            return toSseError(errorMessage, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse<>(errorMessage, null));
    }

    /**
     * IllegalArgumentException 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.error("[IllegalArgumentException] {}", e.getMessage());

        if (isSseRequest(request)) {
            return toSseError(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse<>(e.getMessage(), null));
    }

    /**
     * 인증/인가 관련 예외 처리
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> handleSecurityException(SecurityException e, HttpServletRequest request) {
        log.error("[SecurityException] {}", e.getMessage());

        if (isSseRequest(request)) {
            return toSseError("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponse<>("Unauthorized", null));
    }

    /**
     * Groo 서비스 비즈니스 예외 처리
     */
    @ExceptionHandler(GrooException.class)
    public ResponseEntity<?> handleGrooException(GrooException e, HttpServletRequest request) {
        log.error("[GrooException] Code={}, Message={}", e.getErrorCode().getCode(), e.getMessage());
        String formatted = "[" + e.getErrorCode().getCode() + "]: " + e.getMessage();

        if (isSseRequest(request)) {
            return toSseError(formatted, e.getErrorCode().getHttpStatus());
        }

        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(new CommonResponse<>(formatted, null));
    }

    /**
     * 그 외 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception e, HttpServletRequest request) {
        log.error("[Exception] {}", e.getMessage(), e);

        if (isSseRequest(request)) {
            // 한글 주석: SSE 요청에서는 JSON을 반환하면 안 됨
            return toSseError("Internal SSE error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponse<>("Internal server error", null));
    }

    // ========================================================================
    //                                  유틸 메서드
    // ========================================================================

    /**
     * SSE 요청인지 판별 (Accept 또는 Content-Type 확인)
     */
    private boolean isSseRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String contentType = request.getHeader("Content-Type");

        return (accept != null && accept.contains(MediaType.TEXT_EVENT_STREAM_VALUE)) ||
                (contentType != null && contentType.contains(MediaType.TEXT_EVENT_STREAM_VALUE));
    }

    /**
     * SSE 오류 응답 생성기
     */
    private ResponseEntity<String> toSseError(String msg, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .contentType(MediaType.TEXT_PLAIN)
                .body(msg);
    }
}
