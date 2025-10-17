package com.kcc.groo.review.dao;

import com.kcc.groo.review.data.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface IReviewRepository {

    /**
     * @param userId 리뷰 작성자 ID
     * @param request 리뷰 작성 정보
     * @return void
     * @author uyh
     * @created 2025-09-28
     * 독후감을 등록
     */
    void insertReview(@Param("userId") String userId,
                      @Param("req") ReviewCreateRequest request);

    /**
     * @param userId 리뷰 작성자 ID
     * @param reviewId 수정할 리뷰 ID
     * @param request 수정할 리뷰 정보
     * @return void
     * @author uyh
     * @created 2025-09-28
     * 본인이 작성한 독후감을 수정
     */
    void updateReview(@Param("userId") String userId,
                      @Param("reviewId") Integer reviewId,
                      @Param("req") ReviewUpdateRequest request);

    /**
     * @param userId 리뷰 작성자 ID
     * @param reviewId 삭제할 리뷰 ID
     * @return void
     * @author uyh
     * @created 2025-09-28
     * 본인이 작성한 독후감을 삭제
     */
    void deleteReview(@Param("userId") String userId,
                      @Param("reviewId") Integer reviewId);

    /**
     * @param userId 조회하는 사용자 ID
     * @param reviewId 조회할 리뷰 ID
     * @return ReviewResponse
     * @author uyh
     * @created 2025-09-29
     * 리뷰 ID로 독후감 단건 조회
     */
    ReviewResponse selectReviewById(@Param("userId") String userId,
                                    @Param("reviewId") Integer reviewId);

    /**
     * @param reviewId 조회할 리뷰 ID
     * @return List<CommentResponse>
     * @author uyh
     * @created 2025-09-29
     * 특정 리뷰의 모든 댓글을 조회
     */
    List<CommentResponse> selectCommentsByReview(@Param("reviewId") Integer reviewId);

    /**
     * @param userId 조회하는 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-09-28
     * 사용자가 좋아요한 리뷰 목록을 조회
     */
    List<ReviewResponse> selectReviewsByLikes(@Param("userId") String userId);

    /**
     * @param userId 조회하는 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-09-28
     * 모든 공개 독후감을 조회
     */
    List<ReviewResponse> selectAllReviews(@Param("userId") String userId);

    /**
     * @param userId 조회할 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-09-29
     * 특정 사용자가 작성한 모든 리뷰를 조회
     */
    List<ReviewResponse> selectReviewsByUser(@Param("userId") String userId);

    /**
     * @param userId 조회하는 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-09-28
     * 사용자의 임시저장 글 목록을 조회
     */
    List<ReviewResponse> selectDrafts(@Param("userId") String userId);

    /**
     * @param id 조회할 임시저장 글 ID
     * @param userId 조회하는 사용자 ID
     * @return ReviewResponse
     * @author uyh
     * @created 2025-09-28
     * 임시저장 글 단건 조회
     */
    ReviewResponse selectDraft(@Param("id") int id,
                               @Param("userId") String userId);

    /**
     * @param id 삭제할 임시저장 글 ID
     * @param userId 작성자 ID
     * @return void
     * @author uyh
     * @created 2025-09-28
     * 본인이 작성한 임시저장 글을 삭제
     */
    void deleteDraft(@Param("id") int id,
                     @Param("userId") String userId);

    /**
     * @param userId 좋아요하는 사용자 ID
     * @param reviewId 좋아요할 리뷰 ID
     * @return void
     * @author uyh
     * @created 2025-09-28
     * 리뷰에 좋아요를 추가
     */
    void insertLike(@Param("userId") String userId,
                    @Param("reviewId") Integer reviewId);

    /**
     * @param userId 좋아요 취소하는 사용자 ID
     * @param reviewId 좋아요 취소할 리뷰 ID
     * @return void
     * @author uyh
     * @created 2025-09-28
     * 리뷰 좋아요를 취소
     */
    void deleteLike(@Param("userId") String userId,
                    @Param("reviewId") Integer reviewId);

    /**
     * @param userId 조회하는 사용자 ID
     * @param reviewId 조회할 리뷰 ID
     * @return int
     * @author uyh
     * @created 2025-09-28
     * 사용자가 해당 리뷰에 좋아요를 눌렀는지 확인
     */
    int existsLike(@Param("userId") String userId,
                   @Param("reviewId") Integer reviewId);

    /**
     * @param userId 조회하는 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-09-28
     * 사용자가 좋아요한 모든 독후감을 조회
     */
    List<ReviewResponse> selectLikedReviews(@Param("userId") String userId);

    /**
     * @param reviewId 확인할 리뷰 ID
     * @return boolean
     * @author uyh
     * @created 2025-09-29
     * 좋아요 가능 여부 확인, 임시저장/삭제글 제외
     */
    boolean canLikeReview(@Param("reviewId") Integer reviewId);
    
    /**
     * @param userId 조회하는 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-10-01
     * 팔로잉한 유저들의 독후감을 최신순으로 조회
     */
    List<ReviewResponse> selectReviewsByFollowing(@Param("userId") String userId);
    
    /**
     * @param userId 조회하는 사용자 ID (null 가능)
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-10-01
     * 전체 유저의 독후감을 1주일간 좋아요 많은 순으로 조회
     */
    List<ReviewResponse> selectAllReviewsOrderByLikes(@Param("userId") String userId);
    
    /**
     * @param isbn 조회할 ISBN
     * @param userId 조회하는 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-10-04
     * 특정 ISBN의 모든 공개 독후감을 조회 (비밀글 제외, 임시저장 제외)
     */
    List<ReviewResponse> selectReviewsByIsbn(@Param("isbn") String isbn, 
                                             @Param("userId") String userId);
    
    /**
     * @param category 조회할 카테고리
     * @param userId 조회하는 사용자 ID
     * @param limit 조회할 최대 개수
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-10-04
     * 특정 카테고리의 모든 공개 독후감을 조회 (비밀글 제외, 임시저장 제외)
     */
    List<ReviewResponse> selectReviewsByCategory(@Param("category") String category,
                                                 @Param("userId") String userId,
                                                 @Param("limit") int limit);

    /**
     * @param currentUserId 현재 로그인한 사용자 ID (null 가능)
     * @param targetUserId 조회 대상 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-10-13
     * 특정 유저의 독후감 조회 (본인이면 비밀글 포함, 타인이면 공개글만)
     */
    List<ReviewResponse> selectReviewsByUserWithAccess(@Param("currentUserId") String currentUserId,
                                                       @Param("targetUserId") String targetUserId);

    /**
     * @param currentUserId 현재 로그인한 사용자 ID (null 가능)
     * @param targetUserId 조회 대상 사용자 ID
     * @return List<ReviewResponse>
     * @author uyh
     * @created 2025-10-13
     * 특정 유저가 좋아요한 독후감 조회 (항상 공개글만)
     */
    List<ReviewResponse> selectLikedReviewsByUser(@Param("currentUserId") String currentUserId,
                                                  @Param("targetUserId") String targetUserId);

    /**
     * @param userId 조회할 사용자 ID
     * @return int
     * @author uyh
     * @created 2025-10-16
     * 특정 사용자가 누른 좋아요 수를 조회
     */
    int countLikesByUserId(@Param("userId") String userId);

    /**
     * @param isbn 조회할 ISBN
     * @return int
     * @author uyh
     * @created 2025-10-16
     * 특정 ISBN으로 작성된 독후감 수를 조회
     */
    int countReviewsByIsbn(@Param("isbn") String isbn);

    /**
     * @param startTime 조회 시작 시간
     * @param endTime 조회 종료 시간
     * @return List<TopReviewerDto>
     * @author uyh
     * @created 2025-10-16
     * 특정 기간 동안 가장 많은 독후감을 작성한 사용자 목록을 조회
     */
    List<TopReviewerDto> findTopReviewersByPeriod(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

}