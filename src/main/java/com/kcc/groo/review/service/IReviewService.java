package com.kcc.groo.review.service;

import com.kcc.groo.review.data.dto.*;
import java.util.List;

public interface IReviewService {

    /**
     * @param userId 리뷰 작성자 ID
     * @param request 리뷰 작성 정보
     * @return void
     * @author uyh
     * @created 2025-09-28
     * 독후감을 등록
     */
    void createReview(String userId, ReviewCreateRequest request);

    /**
     * @param userId 리뷰 작성자 ID
     * @param reviewId 수정할 리뷰 ID
     * @param request 수정할 리뷰 정보
     * @return void
     * @author uyh
     * @created 2025-09-28
     * 본인이 작성한 독후감을 수정
     */
    void updateReview(String userId, Integer reviewId, ReviewUpdateRequest request);

    /**
     * @param userId 리뷰 작성자 ID
     * @param reviewId 삭제할 리뷰 ID
     * @return void
     * @author uyh
     * @created 2025-09-28
     * 본인이 작성한 독후감을 삭제
     */
    void deleteReview(String userId, Integer reviewId);

    /**
     * @param userId 조회하는 사용자 ID
     * @param reviewId 조회할 리뷰 ID
     * @return ReviewResponse
     * @author uyh
     * @created 2025-09-29
     * 리뷰 ID로 독후감 단건 조회
     */
    ReviewResponse getReview(String userId, Integer reviewId);

    /**
     * @param userId 조회하는 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-09-28
     * 사용자가 좋아요한 리뷰 목록을 조회
     */
    List<ReviewResponse> getReviewsByLikes(String userId);

    /**
     * @param userId 조회할 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-09-29
     * 특정 사용자가 작성한 모든 리뷰를 조회
     */
    List<ReviewResponse> getReviewsByUser(String userId);

    /**
     * @param userIdOrNull 조회하는 사용자 ID (null 가능)
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-09-28
     * 모든 공개 독후감을 조회
     */
    List<ReviewResponse> getAllReviews(String userIdOrNull);

    /**
     * @param userId 조회하는 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-09-28
     * 사용자의 임시저장 글 목록을 조회
     */
    List<ReviewResponse> getDrafts(String userId);

    /**
     * @param id 조회할 임시저장 글 ID
     * @param userId 조회하는 사용자 ID
     * @return ReviewResponse
     * @author uyh
     * @created 2025-09-28
     * 임시저장 글 단건 조회
     */
    ReviewResponse getDraft(int id, String userId);

    /**
     * @param id 삭제할 임시저장 글 ID
     * @param userId 작성자 ID
     * @return void
     * @author uyh
     * @created 2025-09-28
     * 본인이 작성한 임시저장 글을 삭제
     */
    void deleteDraft(int id, String userId);

    /**
     * @param userId 좋아요하는 사용자 ID
     * @param reviewId 좋아요할 리뷰 ID
     * @return void
     * @author uyh
     * @created 2025-09-28
     * 리뷰에 좋아요를 추가
     */
    void likeReview(String userId, Integer reviewId);

    /**
     * @param userId 좋아요 취소하는 사용자 ID
     * @param reviewId 좋아요 취소할 리뷰 ID
     * @return void
     * @author uyh
     * @created 2025-09-28
     * 리뷰 좋아요를 취소
     */
    void unlikeReview(String userId, Integer reviewId);

    /**
     * @param userId 조회하는 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-09-28
     * 사용자가 좋아요한 모든 독후감을 조회
     */
    List<ReviewResponse> getLikedReviews(String userId);
    
    /**
     * @param userId 조회하는 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-10-01
     * 팔로잉한 유저들의 독후감을 최신순으로 조회
     */
    List<ReviewResponse> getReviewsByFollowing(String userId);
    
    /**
     * @param userId 조회하는 사용자 ID (null 가능)
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-10-01
     * 전체 유저의 독후감을 1주일간 좋아요 많은 순으로 조회
     */
    List<ReviewResponse> getAllReviewsOrderByLikes(String userId);

    /**
     * @param isbn 조회할 ISBN
     * @param userId 조회하는 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-10-04
     * 특정 ISBN의 모든 공개 독후감을 조회
     */
    List<ReviewResponse> getReviewsByIsbn(String isbn, String userId);
    
    /**
     * @param category 조회할 카테고리
     * @param userId 조회하는 사용자 ID
     * @param limit 조회할 최대 개수
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-10-04
     * 특정 카테고리의 모든 공개 독후감을 조회
     */
    List<ReviewResponse> getReviewsByCategory(String category, String userId, int limit);

    /**
     * @param currentUserId 현재 로그인한 사용자 ID (null 가능)
     * @param targetUserId 조회 대상 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-10-13
     * 특정 유저의 독후감 조회 (본인이면 비밀글 포함, 타인이면 공개글만)
     */
    List<ReviewResponse> getReviewsByUserWithAccess(String currentUserId, String targetUserId);

    /**
     * @param currentUserId 현재 로그인한 사용자 ID (null 가능)
     * @param targetUserId 조회 대상 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-10-13
     * 특정 유저가 좋아요한 독후감 조회 (항상 공개글만)
     */
    List<ReviewResponse> getLikedReviewsByUser(String currentUserId, String targetUserId);

    /**
     * @param currentUserId 현재 로그인한 사용자 ID (null 가능)
     * @param targetUserId 조회 대상 사용자 ID
     * @return List<ReviewWithCommentResponseDTO>
     * @author uyh
     * @created 2025-10-28
     * 특정 유저가 댓글 작성한 독후감과 해당 댓글 내용을 함께 조회 (항상 공개글만)
     */
    List<ReviewWithCommentResponseDto> getReviewsWithCommentsByUser(String currentUserId, String targetUserId);

    /**
     * @param userId 조회하는 사용자 ID (null 가능)
     * @param cursorId 마지막으로 조회한 리뷰 ID (null이면 처음부터)
     * @param limit 조회할 개수
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-11-17
     * 전체 리뷰를 커서 기반으로 페이징 조회
     */
    List<ReviewResponse> getAllReviewsWithCursor(String userId, Integer cursorId, int limit);
}