package com.kcc.groo.dashboard.exception;

/**
 * @author uyh
 * @created 2025-01-16
 * Dashboard 커스텀 예외
 */
public class DashboardException extends RuntimeException {

    private final DashboardErrorCode errorCode;

    public DashboardException(DashboardErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public DashboardException(DashboardErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }

    public DashboardErrorCode getErrorCode() {
        return errorCode;
    }

    public String getCode() {
        return errorCode.getCode();
    }
}