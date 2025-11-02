package com.kcc.groo.group.dao;

import com.kcc.groo.group.data.model.GroupComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 독서모임 댓글 관련 데이터베이스 작업을 위한 Repository 인터페이스
 *
 * @author YunSung
 * @created 2025-10-22
 */
@Repository
@Mapper
public interface IGroupCommentRepository {

    /**
     * 독서 모임 게시글에 새로운 댓글 생성
     *
     * @param comment 생성할 댓글 정보
     * @return 삽입된 행의 수
     * @author YunSung
     * @created 2025-10-22
     */
    int insertGroupComment(GroupComment comment);

    /**
     * 특정 독서 모임 게시글의 모든 댓글 조회
     *
     * @param groupId 게시글 ID
     * @return 댓글 목록
     * @author YunSung
     * @created 2025-10-22
     */
    List<GroupComment> selectAllGroupCommentsByGroupId(int groupId);

    /**
     * 독서 모임 게시글의 특정 댓글 조회
     *
     * @param commentId 댓글 ID
     * @return 댓글 정보
     * @author YunSung
     * @created 2025-10-22
     */
    GroupComment selectGroupCommentByCommentId(int commentId);

    /**
     * 독서 모임 게시글의 댓글 내용 수정
     *
     * @param comment 수정할 댓글 정보
     * @return 수정된 행의 수
     * @author YunSung
     * @created 2025-10-22
     */
    int updateGroupComment(GroupComment comment);

    /**
     * 독서 모임 게시글의 특정 댓글 삭제
     *
     * @param commentId 삭제할 댓글 ID
     * @return 삭제된 행의 수
     * @author YunSung
     * @created 2025-10-22
     */
    int deleteGroupCommentByCommentId(@Param("commentId") int commentId, @Param("userId") String userId);
}
