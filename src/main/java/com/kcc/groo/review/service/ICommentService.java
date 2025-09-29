package com.kcc.groo.review.service;

import com.kcc.groo.review.data.dto.CommentRequest;
import com.kcc.groo.review.data.dto.CommentResponse;

import java.util.List;

public interface ICommentService {
    void addComment(String userId, Integer reviewId, CommentRequest req);
    void updateComment(String userId, Integer commentId, String content);
    void deleteComment(String userId, Integer commentId);
    List<CommentResponse> getCommentsByReview(Integer reviewId);
    List<CommentResponse> getCommentsByUser(String userId);
}
