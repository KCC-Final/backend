package com.kcc.groo.review.dao;

import com.kcc.groo.review.data.dto.CommentRequest;
import com.kcc.groo.review.data.dto.CommentResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ICommentRepository {

    /**
     * @param reviewId 댓글을 작성할 리뷰 ID
     * @param userId 댓글 작성자 ID
     * @param req 댓글 내용
     * @return void
     * @author uyh
     * @created 2025-09-29
     * 리뷰에 댓글을 추가
     */
    void insertComment(@Param("reviewId") Integer reviewId,
                       @Param("userId") String userId,
                       @Param("req") CommentRequest req);

    /**
     * @param commentId 수정할 댓글 ID
     * @param userId 댓글 작성자 ID
     * @param content 수정할 댓글 내용
     * @return void
     * @author uyh
     * @created 2025-09-29
     * 본인이 작성한 댓글을 수정
     */
    void updateComment(@Param("commentId") Integer commentId,
                       @Param("userId") String userId,
                       @Param("content") String content);

    /**
     * @param commentId 삭제할 댓글 ID
     * @param userId 댓글 작성자 ID
     * @return void
     * @author uyh
     * @created 2025-09-29
     * 본인이 작성한 댓글을 삭제
     */
    void deleteComment(@Param("commentId") Integer commentId,
                       @Param("userId") String userId);

    /**
     * @param reviewId 조회할 리뷰 ID
     * @return List<CommentResponse>
     * @author uyh
     * @created 2025-09-29
     * 특정 리뷰의 모든 댓글을 조회
     */
    List<CommentResponse> selectCommentsByReview(@Param("reviewId") Integer reviewId);

    /**
     * @param userId 댓글 작성자 ID
     * @return List<CommentResponse>
     * @author uyh
     * @created 2025-09-29
     * 특정 사용자가 작성한 모든 댓글을 조회
     */
    List<CommentResponse> selectCommentsByUser(@Param("userId") String userId);

    /**
     * @param commentId 조회할 댓글 ID
     * @return CommentResponse
     * @author uyh
     * @created 2025-09-29
     * 부모 댓글 단건 조회
     */
    CommentResponse selectCommentById(@Param("commentId") Integer commentId);
}