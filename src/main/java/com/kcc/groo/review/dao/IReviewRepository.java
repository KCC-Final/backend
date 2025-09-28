package com.kcc.groo.review.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kcc.groo.review.data.dto.ReviewCreateRequest;
import com.kcc.groo.review.data.dto.ReviewResponse;
import com.kcc.groo.review.data.dto.ReviewUpdateRequest;

@Mapper
public interface IReviewRepository {

    // CREATE / UPDATE / DELETE
    void insertReview(@Param("userId") String userId, @Param("req") ReviewCreateRequest req);
}
