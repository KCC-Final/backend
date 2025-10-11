package com.kcc.groo.review.service;

import com.kcc.groo.review.dao.ICommentRepository;
import com.kcc.groo.review.dao.IReviewRepository;
import com.kcc.groo.review.data.dto.CommentRequest;
import com.kcc.groo.review.data.dto.CommentResponse;
import com.kcc.groo.review.data.dto.ReviewResponse;
import com.kcc.groo.review.exception.ReviewErrorCode;
import com.kcc.groo.review.exception.ReviewException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final ICommentRepository commentRepository;
    private final IReviewRepository reviewRepository;

    @Transactional
    @Override
    public void addComment(String userId, Integer reviewId, CommentRequest req) {
        // 입력 검증
        validateCommentRequest(req);
        validateUserId(userId);
        
        if (reviewId == null || reviewId <= 0) {
            throw new ReviewException(ReviewErrorCode.INVALID_REVIEW_REQUEST, "Invalid review ID");
        }
        
        // 리뷰 존재 확인
        ReviewResponse review = reviewRepository.selectReviewById(null, reviewId);
        if (review == null) {
            log.warn("[addComment] 독후감을 찾을 수 없음 - reviewId: {}, userId: {}", reviewId, userId);
            throw new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND);
        }
        
        log.debug("[addComment] Found review - reviewId: {}, title: {}, delete_status: {}, temporary: {}, secret: {}", 
                 reviewId, review.getReviewTitle(), review.getStatus(), review.getTemporary(), review.getSecret());
        
        // 삭제된 리뷰에는 댓글 불가
        if (!review.getStatus()) {
            throw new ReviewException(ReviewErrorCode.CANNOT_COMMENT_ON_DELETED);
        }
        
        // 임시저장 글에는 댓글 불가
        if (review.getTemporary()) {
            throw new ReviewException(ReviewErrorCode.CANNOT_COMMENT_ON_DRAFT);
        }
        
        // 비밀글에는 댓글 완전 불가 (작성자도 포함)
        if (review.getSecret()) {
            throw new ReviewException(ReviewErrorCode.CANNOT_COMMENT_ON_SECRET);
        }
        
        // 부모 댓글 검증
        Integer parentId = req.getParentId();
        if (parentId != null) {
            CommentResponse parent = commentRepository.selectCommentById(parentId);
            if (parent == null) {
                throw new ReviewException(ReviewErrorCode.INVALID_PARENT_COMMENT);
            }
            
            // 부모 댓글이 같은 리뷰에 속하는지 확인
            if (!parent.getReviewId().equals(reviewId)) {
                throw new ReviewException(ReviewErrorCode.INVALID_PARENT_COMMENT, 
                                        "Parent comment does not belong to this review");
            }
        }
        
        try {
            commentRepository.insertComment(reviewId, userId, req);
            log.info("[addComment] reviewId: {}, userId: {}, parentId: {}", 
                     reviewId, userId, parentId);
        } catch (Exception e) {
            log.error("[addComment] Failed - reviewId: {}, userId: {}, error: {}", 
                      reviewId, userId, e.getMessage());
            throw new ReviewException(ReviewErrorCode.COMMENT_CREATE_FAILED, e.getMessage());
        }
    }

    @Transactional
    @Override
    public void updateComment(String userId, Integer commentId, String content) {
        // 입력 검증
        validateUserId(userId);
        
        if (commentId == null || commentId <= 0) {
            throw new ReviewException(ReviewErrorCode.INVALID_REVIEW_REQUEST, "Invalid comment ID");
        }
        
        // 👇 댓글 내용 검증 (validateCommentContent 사용)
        validateCommentContent(content);
        
        // 댓글 존재 확인
        CommentResponse existingComment = commentRepository.selectCommentById(commentId);
        if (existingComment == null) {
            throw new ReviewException(ReviewErrorCode.COMMENT_NOT_FOUND);
        }
        
        // 작성자 확인
        if (!existingComment.getUserId().equals(userId)) {
            throw new ReviewException(ReviewErrorCode.NOT_COMMENT_OWNER);
        }
        
        try {
            commentRepository.updateComment(commentId, userId, content);
            log.info("[updateComment] commentId: {}, userId: {}", commentId, userId);
        } catch (Exception e) {
            log.error("[updateComment] Failed - commentId: {}, userId: {}, error: {}", 
                      commentId, userId, e.getMessage());
            throw new ReviewException(ReviewErrorCode.COMMENT_UPDATE_FAILED, e.getMessage());
        }
    }

    @Transactional
    @Override
    public void deleteComment(String userId, Integer commentId) {
        // 입력 검증
        validateUserId(userId);
        
        if (commentId == null || commentId <= 0) {
            throw new ReviewException(ReviewErrorCode.INVALID_REVIEW_REQUEST, "Invalid comment ID");
        }
        
        // 댓글 존재 확인
        CommentResponse existingComment = commentRepository.selectCommentById(commentId);
        if (existingComment == null) {
            throw new ReviewException(ReviewErrorCode.COMMENT_NOT_FOUND);
        }
        
        // 작성자 확인
        if (!existingComment.getUserId().equals(userId)) {
            throw new ReviewException(ReviewErrorCode.NOT_COMMENT_OWNER);
        }
        
        try {
            // CASCADE DELETE: 부모 댓글 삭제 시 대댓글도 자동 삭제됨
            commentRepository.deleteComment(commentId, userId);
            log.info("[deleteComment] commentId: {}, userId: {} - CASCADE DELETE 적용 (대댓글 자동 삭제)", 
                     commentId, userId);
        } catch (Exception e) {
            log.error("[deleteComment] Failed - commentId: {}, userId: {}, error: {}", 
                      commentId, userId, e.getMessage());
            throw new ReviewException(ReviewErrorCode.COMMENT_DELETE_FAILED, e.getMessage());
        }
    }

    @Override
    public List<CommentResponse> getCommentsByReview(Integer reviewId) {
        // 입력 검증
        if (reviewId == null || reviewId <= 0) {
            throw new ReviewException(ReviewErrorCode.INVALID_REVIEW_REQUEST, "Invalid review ID");
        }
        
        // 리뷰 존재 확인
        ReviewResponse review = reviewRepository.selectReviewById(null, reviewId);
        if (review == null) {
            throw new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND);
        }
        
        // 삭제된 리뷰의 댓글은 조회 불가
        if (!review.getStatus()) {
            throw new ReviewException(ReviewErrorCode.CANNOT_ACCESS_DELETED_REVIEW);
        }
        
        // 임시저장 글의 댓글은 조회 불가
        if (review.getTemporary()) {
            throw new ReviewException(ReviewErrorCode.CANNOT_ACCESS_DRAFT_REVIEW);
        }
        
        log.info("[getCommentsByReview] reviewId: {}", reviewId);
        return commentRepository.selectCommentsByReview(reviewId);
    }

    @Override
    public List<CommentResponse> getCommentsByUser(String userId) {
        validateUserId(userId);
        log.info("[getCommentsByUser] userId: {}", userId);
        return commentRepository.selectCommentsByUser(userId);
    }
    
    // ============ Private Validation Methods ============
    
    /**
     * 댓글 요청 검증 (CommentRequest)
     */
    private void validateCommentRequest(CommentRequest request) {
        if (request == null) {
            throw new ReviewException(ReviewErrorCode.INVALID_REVIEW_REQUEST);
        }
        
        validateCommentContent(request.getContent());
    }
    
    /**
     * 댓글 내용 검증 (공통)
     */
    private void validateCommentContent(String content) {
        // null 또는 빈 문자열 검증
        if (content == null || content.trim().isEmpty()) {
            throw new ReviewException(ReviewErrorCode.INVALID_COMMENT_CONTENT);
        }
        
        // 최소 길이 검증
        if (content.trim().length() < 1) {
            throw new ReviewException(ReviewErrorCode.COMMENT_CONTENT_TOO_SHORT);
        }
        
        // 최대 길이 검증 (500자)
        if (content.length() > 500) {
            throw new ReviewException(ReviewErrorCode.COMMENT_CONTENT_TOO_LONG);
        }
    }
    
    /**
     * userId 검증
     */
    private void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new ReviewException(ReviewErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
}