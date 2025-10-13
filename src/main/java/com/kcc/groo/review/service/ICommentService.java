package com.kcc.groo.review.service;

import com.kcc.groo.review.data.dto.CommentRequest;
import com.kcc.groo.review.data.dto.CommentResponse;

import java.util.List;

public interface ICommentService {
    
    /**
     * @param userId 댓글 작성자 ID
     * @param reviewId 댓글을 작성할 리뷰 ID
     * @param req 댓글 내용
     * @return void
     * @author uyh
     * @created 2025-09-29
     * 리뷰에 댓글을 추가
     */
    void addComment(String userId, Integer reviewId, CommentRequest req);
    
    /**
     * @param userId 댓글 작성자 ID
     * @param commentId 수정할 댓글 ID
     * @param content 수정할 댓글 내용
     * @return void
     * @author uyh
     * @created 2025-09-29
     * 본인이 작성한 댓글을 수정
     */
    void updateComment(String userId, Integer commentId, String content);
    
    /**
     * @param userId 댓글 작성자 ID
     * @param commentId 삭제할 댓글 ID
     * @return void
     * @author uyh
     * @created 2025-09-29
     * 본인이 작성한 댓글을 삭제
     */
    void deleteComment(String userId, Integer commentId);
    
    /**
     * @param reviewId 조회할 리뷰 ID
     * @param userId 조회하는 사용자 ID (null 가능)
     * @return List<CommentResponse>
     * @author uyh
     * @created 2025-09-29
     * @updated 2025-10-12
     * 특정 리뷰의 모든 댓글을 조회
     */
    List<CommentResponse> getCommentsByReview(Integer reviewId, String userId);
    
    /**
     * @param userId 댓글 작성자 ID
     * @return List<CommentResponse>
     * @author uyh
     * @created 2025-09-29
     * 특정 사용자가 작성한 모든 댓글을 조회
     */
    List<CommentResponse> getCommentsByUser(String userId);
}