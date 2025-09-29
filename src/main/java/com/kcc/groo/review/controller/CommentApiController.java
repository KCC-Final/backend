package com.kcc.groo.review.controller;

import com.kcc.groo.review.data.dto.CommentRequest;
import com.kcc.groo.review.data.dto.CommentResponse;
import com.kcc.groo.review.service.ICommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "Comment API", description = "독후감 댓글 CRUD API")
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentApiController {

    private final ICommentService commentService;

    @Operation(summary = "댓글 작성", description = "리뷰에 댓글을 작성합니다.")
    @PostMapping("/{reviewId}")
    public ResponseEntity<Void> addComment(
            @PathVariable("reviewId") Integer reviewId,
            Principal principal,
            @RequestBody CommentRequest req) {
        String userId = principal.getName();
        commentService.addComment(userId, reviewId, req);
        return ResponseEntity.ok().build();
    }
