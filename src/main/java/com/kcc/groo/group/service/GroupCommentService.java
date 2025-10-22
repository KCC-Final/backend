package com.kcc.groo.group.service;

import com.kcc.groo.group.dao.IGroupCommentRepository;
import com.kcc.groo.group.data.model.GroupComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 독서모임 댓글 관련 비즈니스 로직을 처리하는 Service
 *
 * @author YunSung
 * @created 2025-10-22
 */
@RequiredArgsConstructor
@Service
public class GroupCommentService implements IGroupCommentService {

    private final IGroupCommentRepository groupCommentRepository;

    /**
     * 독서 모임 게시글에 새로운 댓글 생성
     *
     * @param comment 생성할 댓글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    @Override
    public GroupComment createGroupComment(GroupComment comment) {
        // 독서 모임 댓글 생성
        groupCommentRepository.insertGroupComment(comment);

        // 생성된 독서 모임 댓글 정보 반환 (ID 포함)
        return comment;
    }

    /**
     * 특정 독서 모임 게시글의 모든 댓글 조회
     *
     * @param groupId 게시글 ID
     * @return 댓글 목록
     * @author YunSung
     * @created 2025-10-22
     */
    @Override
    public List<GroupComment> readAllGroupCommentsByGroupId(int groupId) {
        // 특정 독서 모임 게시글의 모든 댓글 조회
        return groupCommentRepository.selectAllGroupCommentsByGroupId(groupId);
    }

    /**
     * 독서 모임 게시글의 특정 댓글 조회
     *
     * @param commentId 댓글 ID
     * @return 댓글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    @Override
    public GroupComment readGroupCommentByCommentId(int commentId) {
        // 독서 모임 게시글의 특정 댓글 조회
        return groupCommentRepository.selectGroupCommentByCommentId(commentId);
    }

    /**
     * 독서 모임 게시글의 댓글 내용 수정
     *
     * @param comment 수정할 댓글 정보
     * @return 수정된 댓글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    @Override
    public GroupComment updateGroupComment(GroupComment comment) {
        // 독서 모임 게시글의 댓글 내용 수정
        groupCommentRepository.updateGroupComment(comment);

        // 수정된 독서 모임 댓글 정보 반환
        return comment;
    }

    /**
     * 독서 모임 게시글의 특정 댓글 삭제
     *
     * @param commentId 삭제할 댓글 ID
     * @author YunSung
     * @created 2025-10-22
     */
    @Override
    public void deleteGroupCommentByCommentId(int commentId) {
        // 독서 모임 게시글의 특정 댓글 삭제
        groupCommentRepository.deleteGroupCommentByCommentId(commentId);
    }
}
