package com.kcc.groo.review.exception;

import org.springframework.http.HttpStatus;

/**
 * Review 도메인 에러 코드 정의
 * @author CI/CD 담당자
 * @created 2025-10-02
 * 
 * 에러 코드 체계: RVW-XXX (Review)
 * - 001~099: 입력값 검증 오류 (400)
 * - 100~199: 인증 오류 (401)
 * - 200~299: 권한 오류 (403)
 * - 300~399: 리소스 없음 (404)
 * - 400~499: 충돌 (409)
 * - 500~599: 비즈니스 규칙 위반 (422)
 * - 900~999: 서버 오류 (500)
 */
public enum ReviewErrorCode {
    
    // 001~099: 입력값 검증 오류 (400 Bad Request)
    INVALID_REVIEW_REQUEST("RVW-001", "잘못된 독후감 요청입니다", HttpStatus.BAD_REQUEST),
    INVALID_REVIEW_CONTENT("RVW-002", "독후감 내용을 입력해주세요", HttpStatus.BAD_REQUEST),
    INVALID_REVIEW_TITLE("RVW-003", "독후감 제목을 입력해주세요", HttpStatus.BAD_REQUEST),
    INVALID_ISBN("RVW-004", "올바른 ISBN 형식이 아닙니다", HttpStatus.BAD_REQUEST),
    INVALID_COMMENT_CONTENT("RVW-005", "댓글 내용을 입력해주세요", HttpStatus.BAD_REQUEST),
    INVALID_PARENT_COMMENT("RVW-006", "존재하지 않는 부모 댓글입니다", HttpStatus.BAD_REQUEST),
    
    // 100~199: 인증 오류 (401 Unauthorized)
    UNAUTHORIZED_ACCESS("RVW-100", "로그인이 필요합니다", HttpStatus.UNAUTHORIZED),
    
    // 200~299: 권한 오류 (403 Forbidden)
    FORBIDDEN_REVIEW_ACCESS("RVW-200", "해당 독후감에 접근할 권한이 없습니다", HttpStatus.FORBIDDEN),
    FORBIDDEN_COMMENT_ACCESS("RVW-201", "해당 댓글에 접근할 권한이 없습니다", HttpStatus.FORBIDDEN),
    FORBIDDEN_DRAFT_ACCESS("RVW-202", "해당 임시저장 글에 접근할 권한이 없습니다", HttpStatus.FORBIDDEN),
    NOT_REVIEW_OWNER("RVW-203", "독후감 작성자만 수정/삭제할 수 있습니다", HttpStatus.FORBIDDEN),
    NOT_COMMENT_OWNER("RVW-204", "댓글 작성자만 수정/삭제할 수 있습니다", HttpStatus.FORBIDDEN),
    NOT_DRAFT_OWNER("RVW-205", "임시저장 글 작성자만 접근할 수 있습니다", HttpStatus.FORBIDDEN),
    
    // 300~399: 리소스 없음 (404 Not Found)
    REVIEW_NOT_FOUND("RVW-300", "독후감을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("RVW-301", "댓글을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    DRAFT_NOT_FOUND("RVW-302", "임시저장 글을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("RVW-303", "사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    
    // 400~499: 충돌 (409 Conflict)
    DUPLICATE_LIKE("RVW-400", "이미 좋아요를 누른 독후감입니다", HttpStatus.CONFLICT),
    LIKE_NOT_FOUND("RVW-401", "좋아요를 누르지 않은 독후감입니다", HttpStatus.CONFLICT),
    CANNOT_LIKE_OWN_REVIEW("RVW-402", "본인이 작성한 독후감에는 좋아요를 할 수 없습니다", HttpStatus.CONFLICT),
    
    // 500~599: 비즈니스 규칙 위반 (422 Unprocessable Entity)
    CANNOT_ACCESS_DELETED_REVIEW("RVW-500", "삭제된 독후감입니다", HttpStatus.UNPROCESSABLE_ENTITY),
    CANNOT_ACCESS_DRAFT_REVIEW("RVW-501", "임시저장 글은 작성자만 볼 수 있습니다", HttpStatus.UNPROCESSABLE_ENTITY),
    CANNOT_ACCESS_SECRET_REVIEW("RVW-502", "비밀글은 작성자만 볼 수 있습니다", HttpStatus.UNPROCESSABLE_ENTITY),
    CANNOT_LIKE_DRAFT_REVIEW("RVW-503", "임시저장 글에는 좋아요를 할 수 없습니다", HttpStatus.UNPROCESSABLE_ENTITY),
    CANNOT_LIKE_DELETED_REVIEW("RVW-504", "삭제된 독후감에는 좋아요를 할 수 없습니다", HttpStatus.UNPROCESSABLE_ENTITY),
    CANNOT_COMMENT_ON_DRAFT("RVW-505", "임시저장 글에는 댓글을 작성할 수 없습니다", HttpStatus.UNPROCESSABLE_ENTITY),
    CANNOT_COMMENT_ON_DELETED("RVW-506", "삭제된 독후감에는 댓글을 작성할 수 없습니다", HttpStatus.UNPROCESSABLE_ENTITY),
    CANNOT_COMMENT_ON_SECRET("RVW-507", "비밀글에는 댓글을 작성할 수 없습니다", HttpStatus.UNPROCESSABLE_ENTITY),
    CANNOT_LIKE_SECRET_REVIEW("RVW-508", "비밀글에는 좋아요를 할 수 없습니다", HttpStatus.UNPROCESSABLE_ENTITY),
    
    // 900~999: 서버 오류 (500 Internal Server Error)
    DATABASE_ERROR("RVW-900", "데이터베이스 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    REVIEW_CREATE_FAILED("RVW-901", "독후감 작성에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    REVIEW_UPDATE_FAILED("RVW-902", "독후감 수정에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    REVIEW_DELETE_FAILED("RVW-903", "독후감 삭제에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    COMMENT_CREATE_FAILED("RVW-904", "댓글 작성에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    COMMENT_UPDATE_FAILED("RVW-905", "댓글 수정에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    COMMENT_DELETE_FAILED("RVW-906", "댓글 삭제에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
    
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
    
    ReviewErrorCode(String code, String message, HttpStatus httpStatus) {
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