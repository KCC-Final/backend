package com.kcc.groo.common.exception;

import lombok.Getter;

/**
 * Groo 애플리케이션의 공통 예외 클래스
 *
 * @author Yunsung
 * @created 2025-10-22
 */
@Getter
public class GrooException extends RuntimeException {

    private final GrooErrorCode errorCode;

    public GrooException(GrooErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public GrooException(GrooErrorCode errorCode, String additionalMessage) {
        super(errorCode.getMessage() + " : " + additionalMessage);
        this.errorCode = errorCode;
    }

}
