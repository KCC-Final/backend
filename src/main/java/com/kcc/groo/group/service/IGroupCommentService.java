package com.kcc.groo.group.service;

import com.kcc.groo.group.data.model.GroupComment;

import java.util.List;

/**
 * 독서모임 댓글 관련 비즈니스 로직을 처리하는 Service 인터페이스
 *
 * @author YunSung
 * @created 2025-10-22
 */
public interface IGroupCommentService {

    /**
     * 독서 모임 게시글에 새로운 댓글 생성
     *
     * @param comment 생성할 댓글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    GroupComment createGroupComment(GroupComment comment);

    /**
     * 특정 독서 모임 게시글의 모든 댓글 조회
     *
     * @param groupId 게시글 ID
     * @return 댓글 목록
     * @author YunSung
     * @created 2025-10-22
     */
    List<GroupComment> readAllGroupCommentsByGroupId(int groupId);

    /**
     * 독서 모임 게시글의 특정 댓글 조회
     *
     * @param commentId 댓글 ID
     * @return 댓글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    GroupComment readGroupCommentByCommentId(int commentId);

    /**
     * 독서 모임 게시글의 댓글 내용 수정
     *
     * @param comment 수정할 댓글 정보
     * @return 수정된 댓글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    GroupComment updateGroupComment(GroupComment comment);

    /**
     * 독서 모임 게시글의 특정 댓글 삭제
     *
     * @param commentId 삭제할 댓글 ID
     * @author YunSung
     * @created 2025-10-22
     */
    void deleteGroupCommentByCommentId(int commentId);
}
