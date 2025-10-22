package com.kcc.groo.group.service;

import com.kcc.groo.common.exception.GrooException;
import com.kcc.groo.group.dao.IGroupCommentRepository;
import com.kcc.groo.group.dao.IGroupRepository;
import com.kcc.groo.group.data.dto.GroupCommentRequestDTO;
import com.kcc.groo.group.data.model.GroupComment;
import com.kcc.groo.group.data.model.Groups;
import com.kcc.groo.group.exception.GroupErrorCode;
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

    private final IGroupRepository groupRepository;

    private final IGroupCommentRepository groupCommentRepository;

    /**
     * 독서 모임 게시글에 새로운 댓글 생성
     *
     * @author YunSung
     * @created 2025-10-22
     * @modified 2025-10-23
     * 댓글 생성 성공/실패 확인 과정 추가
     */
    @Override
    public GroupComment createGroupComment(GroupCommentRequestDTO comment, int groupId, String userId) {
        // 작성할 독서 모임 댓글 정보 설정
        GroupComment newGroupComment = new GroupComment();
        newGroupComment.setGroupId(groupId);
        newGroupComment.setUserId(userId);
        newGroupComment.setContent(comment.getContent());
        newGroupComment.setFlag(comment.getFlag());
        newGroupComment.setParentId(comment.getParentId());

        // 독서 모임 댓글 생성
        int result = groupCommentRepository.insertGroupComment(newGroupComment);

        // 생성 실패 시 예외 발생
        if (result == 0) {
            throw new GrooException(GroupErrorCode.FAILED_CREATE_GROUP_COMMENT);
        }

        // 생성된 독서 모임 댓글 정보 반환 (ID 포함)
        return newGroupComment;
    }

    /**
     * 특정 독서 모임 게시글의 모든 댓글 조회
     *
     * @return 댓글 목록
     * @author YunSung
     * @created 2025-10-22
     * @modified 2025-10-23
     * 존재하지 않는 독서 모임 게시글에 대한 예외 처리 추가
     */
    @Override
    public List<GroupComment> readAllGroupCommentsByGroupId(int groupId) {
        // 댓글 목록을 조회할 독서 모임 게시글 조회
        Groups readGroup = groupRepository.selectGroupByGroupId(groupId);

        // 존재하지 않는 독서 모임 게시글인 경우 예외 발생
        if (readGroup == null) {
            throw new GrooException(GroupErrorCode.NOT_FOUND_GROUP_TO_READ_COMMENT);
        }

        // 특정 독서 모임 게시글의 모든 댓글 조회
        return groupCommentRepository.selectAllGroupCommentsByGroupId(groupId);
    }

    /**
     * 독서 모임 게시글의 특정 댓글 조회
     *
     * @return 댓글 정보
     * @author YunSung
     * @created 2025-10-22
     * @modified 2025-10-23
     * 존재하지 않는 독서 모임 댓글에 대한 예외 처리 추가
     */
    @Override
    public GroupComment readGroupCommentByCommentId(int commentId) {
        // 독서 모임 게시글의 특정 댓글 조회
        GroupComment comment = groupCommentRepository.selectGroupCommentByCommentId(commentId);

        // 존재하지 않는 독서 모임 댓글인 경우 예외 발생
        if (comment == null) {
            throw new GrooException(GroupErrorCode.NOT_FOUND_GROUP_COMMENT_TO_READ);
        }

        // 조회한 독서 모임 댓글 정보 반환
        return comment;
    }

    /**
     * 독서 모임 게시글의 댓글 내용 수정
     *
     * @return 수정된 댓글 정보
     * @author YunSung
     * @created 2025-10-22
     * @modified 2025-10-23
     * 수정할 댓글 존재 여부, 수정 권한, 수정 성공/실패 확인 과정 추가
     */
    @Override
    public GroupComment updateGroupComment(GroupCommentRequestDTO comment, int commentId, String userId) {
        // 수정할 독서 모임 게시글의 댓글 조회
        GroupComment targetComment = groupCommentRepository.selectGroupCommentByCommentId(commentId);

        // 수정할 독서 모임 댓글 ID의 댓글이 존재하지 않으면 예외 발생
        if (targetComment == null) {
            throw new GrooException(GroupErrorCode.NOT_FOUND_GROUP_COMMENT_TO_UPDATE);
        }

        // 수정할 독서 모임 댓글 작성자와 기존 작성자가 다르면 예외 발생
        if (!targetComment.getUserId().equals(userId)) {
            throw new GrooException(GroupErrorCode.FORBIDDEN_UPDATE_GROUP_COMMENT);
        }

        // 수정할 독서 모임 댓글 정보 설정
        targetComment.setContent(comment.getContent());
        targetComment.setFlag(comment.getFlag());


        // 독서 모임 게시글의 댓글 내용 수정
        int result = groupCommentRepository.updateGroupComment(targetComment);

        // 수정 실패 시 예외 발생
        if (result == 0) {
            throw new GrooException(GroupErrorCode.FAILED_UPDATE_GROUP_COMMENT);
        }

        // 수정된 독서 모임 댓글 정보 반환
        return targetComment;
    }

    /**
     * 독서 모임 게시글의 특정 댓글 삭제
     *
     * @author YunSung
     * @created 2025-10-22
     * @modified 2025-10-23
     * 삭제할 댓글 존재 여부, 삭제 권한, 삭제 성공/실패 확인 과정 추가
     */
    @Override
    public void deleteGroupCommentByCommentId(int commentId, String userId) {
        // 삭제할 독서 모임 게시글의 댓글 조회
        GroupComment targetComment = groupCommentRepository.selectGroupCommentByCommentId(commentId);

        // 삭제할 독서 모임 댓글 ID의 댓글이 존재하지 않으면 예외 발생
        if (targetComment == null) {
            throw new GrooException(GroupErrorCode.NOT_FOUND_GROUP_COMMENT_TO_DELETE);
        }

        // 삭제할 독서 모임 댓글 작성자와 기존 작성자가 다르면 예외 발생
        if (!targetComment.getUserId().equals(userId)) {
            throw new GrooException(GroupErrorCode.FORBIDDEN_DELETE_GROUP_COMMENT);
        }

        // 독서 모임 게시글의 특정 댓글 삭제
        int result = groupCommentRepository.deleteGroupCommentByCommentId(commentId);

        // 삭제 실패 시 예외 발생
        if (result == 0) {
            throw new GrooException(GroupErrorCode.FAILED_DELETE_GROUP_COMMENT);
        }
    }
}
