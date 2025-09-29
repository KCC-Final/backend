package com.kcc.groo.review.dao;

import com.kcc.groo.review.data.dto.CommentRequest;
import com.kcc.groo.review.data.dto.CommentResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ICommentRepository {

    void insertComment(@Param("reviewId") Integer reviewId,
                       @Param("userId") String userId,
                       @Param("req") CommentRequest req);

