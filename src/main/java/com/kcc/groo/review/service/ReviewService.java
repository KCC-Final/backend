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

    @Override
    public List<ReviewResponse> getDrafts(String userId) {
        return reviewRepository.selectDrafts(userId);
    }

    @Override
    public ReviewResponse getDraft(int id, String userId) {
        return reviewRepository.selectDraft(id, userId);
    }

    @Override
    public void deleteDraft(int id, String userId) {
        reviewRepository.deleteDraft(id, userId);
    }

    @Transactional
    @Override
    public void likeReview(String userId, Integer reviewId) {
        // ✅ 임시저장/삭제글 검증
        if (!reviewRepository.canLikeReview(reviewId)) {
            throw new IllegalStateException("임시저장 글이나 삭제된 글에는 좋아요를 누를 수 없습니다.");
        }

        // 중복 방지
        if (reviewRepository.existsLike(userId, reviewId) == 0) {
            reviewRepository.insertLike(userId, reviewId);
        }
    }

    @Override
    public void unlikeReview(String userId, Integer reviewId) {
        reviewRepository.deleteLike(userId, reviewId);
    }

    @Override
    public List<ReviewResponse> getLikedReviews(String userId) {
        return reviewRepository.selectLikedReviews(userId);
    }
}
