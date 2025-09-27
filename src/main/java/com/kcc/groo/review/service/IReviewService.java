// com.kcc.groo.review.service.IReviewService.java
package com.kcc.groo.review.service;

import com.kcc.groo.review.data.dto.*;

import java.util.List;

public interface IReviewService {
    void createReview(String userId, ReviewCreateRequest request);
}
