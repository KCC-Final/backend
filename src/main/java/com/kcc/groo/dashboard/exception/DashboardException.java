package com.kcc.groo.dashboard.exception;

/**
 * Dashboard 도메인에서 발생하는 비즈니스 예외
 * @author uyh
 * @created 2025-01-16
 */
public class DashboardException extends RuntimeException {

    private final DashboardErrorCode errorCode;

    public DashboardException(DashboardErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public DashboardException(DashboardErrorCode errorCode, String additionalMessage) {
        super(errorCode.getMessage() + " : " + additionalMessage);
        this.errorCode = errorCode;
    }

    public DashboardErrorCode getErrorCode() {
        return errorCode;
    }
}