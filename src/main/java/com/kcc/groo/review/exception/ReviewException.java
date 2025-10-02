package com.kcc.groo.review.exception;

/**
 * Review 도메인에서 발생하는 비즈니스 예외
 * @author CI/CD 담당자
 * @created 2025-10-02
 */
public class ReviewException extends RuntimeException {
    
    private final ReviewErrorCode errorCode;
    
    public ReviewException(ReviewErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    public ReviewException(ReviewErrorCode errorCode, String additionalMessage) {
        super(errorCode.getMessage() + " : " + additionalMessage);
        this.errorCode = errorCode;
    }
    
    public ReviewErrorCode getErrorCode() {
        return errorCode;
    }
}