package com.kcc.groo.common.exception;

import com.kcc.groo.common.dto.CommonResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @author kys
     * @created 2025-09-23
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
     * Query 파라미터(@RequestParam) 및 Path 파라미터(@PathVariable)에 대한 @Validated 검증 실패 처리
     *
     * @author YunSung
     * @created 2025-10-23
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
        // 제약 조건 위반 예외에서 첫 번째 위반 항목 가져오기
        String errorMessage = e.getConstraintViolations().iterator().next().getMessage();

        // 로그에 경고 메시지 기록
        log.error("[ConstraintViolationException] {}", errorMessage);

        // 400 Bad Request 응답 생성
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse<>(String.format(errorMessage), null));
    }

    /**
     * @author kys
     * @created 2025-09-23
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
     * @created 2025-09-23
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
     * Groo 서비스의 비즈니스 예외 처리
     *
     * @author YunSung
     * @created 2025-10-22
     */
    @ExceptionHandler(GrooException.class)
    public ResponseEntity<CommonResponse<?>> handleGrooException(GrooException e) {
        log.error("[GrooException] ErrorCode: {}, Message: {}", e.getErrorCode().getCode(), e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(new CommonResponse<>("[" + e.getErrorCode().getCode() + "]: " + e.getMessage(), null));
    }

    /**
     * @author kys
     * @created 2025-09-23
     * 그 외 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<?>> handleGeneralException(Exception e) {
        log.error("[Exception] {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponse<>("Internal server error", null));
    }
}
