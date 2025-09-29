package com.kcc.groo.review.service;

import com.kcc.groo.review.dao.ICommentRepository;
import com.kcc.groo.review.data.dto.CommentRequest;
import com.kcc.groo.review.data.dto.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements ICommentService {

    private final ICommentRepository commentRepository;

    @Transactional
    @Override
    public void addComment(String userId, Integer reviewId, CommentRequest req) {
        Integer parentId = req.getParentId();

        // ✅ parentId가 넘어왔는데 존재하지 않으면 null 처리
        if (parentId != null) {
            CommentResponse parent = commentRepository.selectCommentById(parentId);
            if (parent == null) {
                parentId = null; // 부모 없음 → 그냥 최상위 댓글로 처리
            }
        }

        // parentId를 다시 세팅
        req.setParentId(parentId);

        commentRepository.insertComment(reviewId, userId, req);
    }
