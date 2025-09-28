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
    }
}
