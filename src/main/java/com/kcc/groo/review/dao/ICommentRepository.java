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

    void updateComment(@Param("commentId") Integer commentId,
                       @Param("userId") String userId,
                       @Param("content") String content);

    void deleteComment(@Param("commentId") Integer commentId,
                       @Param("userId") String userId);

    List<CommentResponse> selectCommentsByReview(@Param("reviewId") Integer reviewId);

    List<CommentResponse> selectCommentsByUser(@Param("userId") String userId);

    // ✅ 부모 댓글 단건 조회용
    CommentResponse selectCommentById(@Param("commentId") Integer commentId);
}

