package com.kcc.groo.dashboard.exception;

import org.springframework.http.HttpStatus;

/**
 * @author uyh
 * @created 2025-01-16
 * Dashboard 에러 코드 정의
 */
public enum DashboardErrorCode {

    // 400 Bad Request
    INVALID_YEAR("DASH-400-001", "유효하지 않은 연도입니다", HttpStatus.BAD_REQUEST),
    INVALID_MONTH("DASH-400-002", "유효하지 않은 월입니다 (1-12)", HttpStatus.BAD_REQUEST),
    INVALID_YEAR_MONTH_FORMAT("DASH-400-003", "올바르지 않은 연월 형식입니다 (YYYY-MM)", HttpStatus.BAD_REQUEST),
    YEAR_OUT_OF_RANGE("DASH-400-004", "연도는 1900년부터 현재+1년까지 가능합니다", HttpStatus.BAD_REQUEST),
    FUTURE_DATE_NOT_ALLOWED("DASH-400-005", "미래 날짜는 조회할 수 없습니다", HttpStatus.BAD_REQUEST),

    // 401 Unauthorized
    UNAUTHORIZED("DASH-401-001", "인증이 필요합니다", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("DASH-401-002", "유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED),

    // 500 Internal Server Error
    DATABASE_ERROR("DASH-500-001", "데이터베이스 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    STATS_RETRIEVAL_FAILED("DASH-500-002", "통계 조회에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    DashboardErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}