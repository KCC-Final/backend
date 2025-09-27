package com.kcc.groo.review.service;

import com.kcc.groo.review.dao.IReviewRepository;
import com.kcc.groo.review.data.dto.*;
import com.kcc.groo.review.data.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final IReviewRepository reviewRepository;

    @Override
    public void createReview(String userId, ReviewCreateRequest request) {
    	
        System.out.println(">>> [DEBUG] userId from JWT = " + userId);

        Review review = new Review();
        review.setISBN(request.getISBN());
        review.setReviewTitle(request.getReviewTitle());
        review.setReviewContent(request.getReviewContent());
        review.setSecret(request.getSecret());
        review.setTemporary(request.getTemporary());
        review.setUserId(userId);
        review.setCodeId(request.getCodeId());
        reviewRepository.insertReview(review);
    }
}
