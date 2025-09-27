// com.kcc.groo.review.dao.IReviewRepository.java
package com.kcc.groo.review.dao;

import com.kcc.groo.review.data.model.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; 

import java.util.List;

@Mapper
public interface IReviewRepository {
    void insertReview(Review review);
}
