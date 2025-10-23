package com.kcc.groo.dashboard.exception;

import com.kcc.groo.common.exception.GrooErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Dashboard 도메인 에러 코드 정의
 *
 * @author uyh
 * @created 2025-01-16
 * <p>
 * 에러 코드 체계: DSH-XXX (Dashboard)
 * - 001~099: 입력값 검증 오류 (400)
 * - 100~199: 인증 오류 (401)
 * - 200~299: 권한 오류 (403)
 * - 300~399: 리소스 없음 (404)
 * - 400~499: 충돌 (409)
 * - 500~599: 비즈니스 규칙 위반 (422)
 * - 900~999: 서버 오류 (500)
 */
public enum DashboardErrorCode implements GrooErrorCode {

    // 001~099: 입력값 검증 오류 (400 Bad Request)
    INVALID_YEAR("DSH-001", "잘못된 연도입니다", HttpStatus.BAD_REQUEST),
    INVALID_MONTH("DSH-002", "잘못된 월입니다. 1-12 사이의 값을 입력해주세요", HttpStatus.BAD_REQUEST),
    INVALID_USER_ID("DSH-003", "사용자 ID가 유효하지 않습니다", HttpStatus.BAD_REQUEST),
    YEAR_OUT_OF_RANGE("DSH-004", "연도는 1900년부터 2100년 사이여야 합니다", HttpStatus.BAD_REQUEST),

    // 100~199: 인증 오류 (401 Unauthorized)
    UNAUTHORIZED_ACCESS("DSH-100", "로그인이 필요합니다", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("DSH-101", "유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED),

    // 200~299: 권한 오류 (403 Forbidden)
    FORBIDDEN_DASHBOARD_ACCESS("DSH-200", "대시보드에 접근할 권한이 없습니다", HttpStatus.FORBIDDEN),

    // 300~399: 리소스 없음 (404 Not Found)
    USER_NOT_FOUND("DSH-300", "사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    NO_DATA_FOUND("DSH-301", "조회된 데이터가 없습니다", HttpStatus.NOT_FOUND),

    // 900~999: 서버 오류 (500 Internal Server Error)
    DATABASE_ERROR("DSH-900", "데이터베이스 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    SUMMARY_STATS_FAILED("DSH-901", "대시보드 통계 조회에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    MONTHLY_STATS_FAILED("DSH-902", "월별 통계 조회에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    MONTHLY_REPORT_FAILED("DSH-903", "월간 리포트 조회에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    DashboardErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}