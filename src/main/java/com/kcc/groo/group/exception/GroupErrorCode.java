package com.kcc.groo.group.exception;

import com.kcc.groo.common.exception.GrooErrorCode;
import org.springframework.http.HttpStatus;

/**
 * 독서모임 관련 에러 코드 정의
 *
 * @author YunSung
 * @created 2025-10-23
 */
public enum GroupErrorCode implements GrooErrorCode {
    // 001~099: 인증 / 인가 오류 (401 Unauthorized)

    // 100~299: 입력값 검증 오류 (400 Bad Request)
    INVALID_GROUP_NAME_BLANK("GRP-100", "게시글 제목은 필수 입력값입니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_NAME_SIZE("GRP-101", "게시글 제목은 1~100자 이내로 입력해주세요", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_BOOK_TITLE_SIZE("GRP-105", "독서모임 도서명은 1~200자 이내로 입력해주세요", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_HEADCOUNTMIN_MIN("GRP-110", "모집 최소 인원은 1명 이상이어야 합니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_HEADCOUNTMAX_MIN("GRP-115", "모집 최대 인원은 1명 이상이어야 합니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_HEADCOUNTMAX_MAX("GRP-116", "모집 최대 인원은 12명 이하이어야 합니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_HEADCOUNTMAX_LESS_THAN_MIN("GRP-117", "모집 최대 인원은 최소 인원보다 크거나 같아야 합니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_CONTENT_BLANK("GRP-120", "게시글 내용은 필수 입력값입니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_CONTENT_SIZE("GRP-121", "게시글 내용은 1~1000자 이내로 입력해주세요", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_STYLE_PATTERN("GRP-125", "모임 스타일은 '자유', '독서', '토론' 중 하나여야 합니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_STATUS_NULL("GRP-130", "모집 상태는 필수 입력값입니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_ENDDATE_NULL("GRP-135", "모집 마감일은 필수 입력값입니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_ENDDATE_PAST("GRP-136", "모집 마감일은 현재 날짜 이후여야 합니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_CODE_NULL("GRP-140", "모임의 지역 코드는 필수 입력값입니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_CODE_MIN("GRP-141", "모임의 지역 코드는 1 이상의 값이어야 합니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_CODE_MAX("GRP-142", "모임의 지역 코드는 17 이하의 값이어야 합니다", HttpStatus.BAD_REQUEST),
    INVALID_FILTER_STYLE("GRP-160", "필터링 style값은 'discussion', 'reading', 'free' 중 하나여야 합니다", HttpStatus.BAD_REQUEST),
    INVALID_FILTER_LOCATION("GRP-161", "필터링 location값은 1에서 17 사이의 값이어야 합니다", HttpStatus.BAD_REQUEST),
    INVALID_FILTER_PAGE_MIN("GRP-162", "페이지는 1 이상의 값이어야 합니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_COMMENT_COMMENT_BLANK("GRP-200", "댓글 내용은 필수 입력값입니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_COMMENT_COMMENT_SIZE("GRP-201", "댓글 내용은 1~500자 이내로 입력해주세요", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_COMMENT_FLAG_NULL("GRP-205", "공개 여부는 필수 입력값입니다", HttpStatus.BAD_REQUEST),
    INVALID_GROUP_COMMENT_PARENT_ID_MIN("GRP-210", "부모 댓글 ID는 1 이상의 값이어야 합니다", HttpStatus.BAD_REQUEST),

    // 300~399: 리소스 없음 (404 Not Found)
    NOT_FOUND_GROUP_TO_READ("GRP-300", "조회할 독서모임 게시글이 없습니다", HttpStatus.NOT_FOUND),
    NOT_FOUND_GROUP_TO_UPDATE("GRP-301", "수정할 독서모임 게시글이 없습니다", HttpStatus.NOT_FOUND),
    NOT_FOUND_GROUP_TO_DELETE("GRP-302", "삭제할 독서모임 게시글이 없습니다", HttpStatus.NOT_FOUND),
    NOT_FOUND_GROUP_TO_READ_COMMENT("GRP-303", "댓글 조회할 독서모임 게시글이 없습니다", HttpStatus.NOT_FOUND),
    NOT_FOUND_GROUP_TO_SCRAP("GRP-304", "스크랩할 독서모임 게시글이 없습니다", HttpStatus.NOT_FOUND),
    NOT_FOUND_GROUP_TO_CHECK_SCRAP("GRP-305", "스크랩 상태를 확인할 독서모임 게시글이 없습니다", HttpStatus.NOT_FOUND),
    NOT_FOUND_GROUP_TO_CANCEL_SCRAP("GRP-306", "스크랩 취소할 독서모임 게시글이 없습니다", HttpStatus.NOT_FOUND),
    NOT_FOUND_GROUP_COMMENT_TO_READ("GRP-320", "조회할 독서모임 댓글이 없습니다", HttpStatus.NOT_FOUND),
    NOT_FOUND_GROUP_COMMENT_TO_UPDATE("GRP-321", "수정할 독서모임 댓글이 없습니다", HttpStatus.NOT_FOUND),
    NOT_FOUND_GROUP_COMMENT_TO_DELETE("GRP-322", "삭제할 독서모임 댓글이 없습니다", HttpStatus.NOT_FOUND),

    // 400~499: 권한 오류 (403 Forbidden)
    FORBIDDEN_UPDATE_GROUP("GRP-400", "독서모임 게시글을 수정할 권한이 없습니다", HttpStatus.FORBIDDEN),
    FORBIDDEN_DELETE_GROUP("GRP-401", "독서모임 게시글을 삭제할 권한이 없습니다", HttpStatus.FORBIDDEN),
    FORBIDDEN_UPDATE_GROUP_COMMENT("GRP-402", "독서모임 댓글을 수정할 권한이 없습니다", HttpStatus.FORBIDDEN),
    FORBIDDEN_DELETE_GROUP_COMMENT("GRP-403", "독서모임 댓글을 삭제할 권한이 없습니다", HttpStatus.FORBIDDEN),

    // 500~899: 그 외
    ALREADY_SCRAPPED_GROUP("GRP-500", "이미 스크랩한 독서모임입니다", HttpStatus.CONFLICT),
    NOT_SCRAPPED_GROUP("GRP-501", "스크랩하지 않은 독서모임입니다", HttpStatus.CONFLICT),

    // 900~999: 서버 오류 (500 Internal Server Error)
    FAILED_CREATE_GROUP("GRP-900", "독서모임 게시글 생성에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_UPDATE_GROUP("GRP-901", "독서모임 게시글 수정에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_DELETE_GROUP("GRP-902", "독서모임 게시글 삭제에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_CREATE_GROUP_COMMENT("GRP-903", "독서모임 댓글 생성에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_UPDATE_GROUP_COMMENT("GRP-904", "독서모임 댓글 수정에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_DELETE_GROUP_COMMENT("GRP-905", "독서모임 댓글 삭제에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_SCRAP_GROUP("GRP-906", "독서모임 스크랩 생성에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    FAILED_CANCEL_SCRAP_GROUP("GRP-907", "독서모임 스크랩 삭제에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);


    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    GroupErrorCode(String code, String message, HttpStatus httpStatus) {
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
