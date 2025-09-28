package com.kcc.groo.review.service;

import com.kcc.groo.review.data.dto.*;
import java.util.List;

public interface IReviewService {

    void createReview(String userId, ReviewCreateRequest request);

    void updateReview(String userId, Integer reviewId, ReviewUpdateRequest request);

    void deleteReview(String userId, Integer reviewId);

    ReviewResponse getReview(String userId, Integer reviewId);

    List<ReviewResponse> getReviewsByLikes(String userId);
    
    List<ReviewResponse> getReviewsByUser(String userId);
    
    List<ReviewResponse> getAllReviews(String userIdOrNull);

    List<ReviewResponse> getDrafts(String userId);

    ReviewResponse getDraft(int id, String userId);

    void deleteDraft(int id, String userId);

    void likeReview(String userId, Integer reviewId);

    void unlikeReview(String userId, Integer reviewId);

    List<ReviewResponse> getLikedReviews(String userId);
}
