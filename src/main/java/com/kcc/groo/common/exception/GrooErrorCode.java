package com.kcc.groo.common.exception;

import org.springframework.http.HttpStatus;

/**
 * 공통 에러 코드 인터페이스
 *
 * @author Yunsung
 * @created 2025-10-22
 */
public interface GrooErrorCode {

    /**
     * 에러 코드 반환
     *
     * @return 에러 코드 문자열
     * @author Yunsung
     * @created 2025-10-22
     */
    String getCode();

    /**
     * 에러 코드 반환
     *
     * @return 에러 코드 문자열
     * @author Yunsung
     * @created 2025-10-22
     */
    String getMessage();

    /**
     * HTTP 상태 코드 반환
     *
     * @return HttpStatus 객체
     * @author Yunsung
     * @created 2025-10-22
     */
    HttpStatus getHttpStatus();
}
