package com.kcc.groo.review.service;

import com.kcc.groo.review.data.dto.*;
import java.util.List;

public interface IReviewService {
    // 생성/수정/삭제

    void createReview(String userId, ReviewCreateRequest request);

    void updateReview(String userId, Integer reviewId, ReviewUpdateRequest request);

    void deleteReview(String userId, Integer reviewId);

    ReviewResponse getReview(String userId, Integer reviewId);

    List<ReviewResponse> getAllReviews(String userIdOrNull);
}
