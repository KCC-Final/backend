package com.kcc.groo.review.service;

import com.kcc.groo.review.dao.IReviewRepository;
import com.kcc.groo.review.data.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final IReviewRepository reviewRepository;

    @Override
    public void createReview(String userId, ReviewCreateRequest request) {
        reviewRepository.insertReview(userId, request);
    }

    @Override
    public void updateReview(String userId, Integer reviewId, ReviewUpdateRequest request) {
        reviewRepository.updateReview(userId, reviewId, request);
    }

    @Override
    public void deleteReview(String userId, Integer reviewId) {
        reviewRepository.deleteReview(userId, reviewId);
    }

    @Override
    public ReviewResponse getReview(String userId, Integer reviewId) {
        return reviewRepository.selectReviewById(userId, reviewId);
    }

    @Override
    public List<ReviewResponse> getAllReviews(String userIdOrNull) {
        return reviewRepository.selectAllReviews(userIdOrNull);
    }

    }
}
