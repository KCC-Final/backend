package com.kcc.groo.review.service;

import com.kcc.groo.review.data.dto.CommentRequest;
import com.kcc.groo.review.data.dto.CommentResponse;

import java.util.List;

public interface ICommentService {
    void addComment(String userId, Integer reviewId, CommentRequest req);
