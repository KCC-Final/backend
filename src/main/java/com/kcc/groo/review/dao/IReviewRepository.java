package com.kcc.groo.review.dao;

import com.kcc.groo.review.data.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IReviewRepository {

    void insertReview(@Param("userId") String userId,
                      @Param("req") ReviewCreateRequest request);

    void updateReview(@Param("userId") String userId,
                      @Param("reviewId") Integer reviewId,
                      @Param("req") ReviewUpdateRequest request);

    void deleteReview(@Param("userId") String userId,
                      @Param("reviewId") Integer reviewId);

    ReviewResponse selectReviewById(@Param("userId") String userId,
                                    @Param("reviewId") Integer reviewId);
    List<ReviewResponse> selectReviewsByLikes(@Param("userId") String userId);

    List<ReviewResponse> selectAllReviews(@Param("userId") String userId);
    
    // 내가 작성한 리뷰 조회
    List<ReviewResponse> selectReviewsByUser(@Param("userId") String userId);

    List<ReviewResponse> selectDrafts(@Param("userId") String userId);

    ReviewResponse selectDraft(@Param("id") int id,
                               @Param("userId") String userId);

    void deleteDraft(@Param("id") int id,
                     @Param("userId") String userId);

    void insertLike(@Param("userId") String userId,
                    @Param("reviewId") Integer reviewId);

    void deleteLike(@Param("userId") String userId,
                    @Param("reviewId") Integer reviewId);

    int existsLike(@Param("userId") String userId,
                   @Param("reviewId") Integer reviewId);

    List<ReviewResponse> selectLikedReviews(@Param("userId") String userId);

    // ✅ 좋아요 가능 여부 확인 (임시저장/삭제글 제외)
    boolean canLikeReview(@Param("reviewId") Integer reviewId);
}
