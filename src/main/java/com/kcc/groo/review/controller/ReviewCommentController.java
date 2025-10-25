package com.kcc.groo.review.controller;

import com.kcc.groo.review.data.dto.CommentRequest;
import com.kcc.groo.review.data.dto.CommentResponse;
import com.kcc.groo.review.service.IReviewCommentService;
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
public class ReviewCommentController {

    private final IReviewCommentService commentService;

    /**
     * @param reviewId  댓글을 작성할 리뷰 ID
     * @param principal 인증된 사용자 정보
     * @param req       댓글 내용
     * @return ResponseEntity<Void>
     * @author uyh
     * @created 2025-09-29
     * 리뷰에 댓글을 작성
     */
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

    /**
     * @param commentId 수정할 댓글 ID
     * @param principal 인증된 사용자 정보
     * @param req       수정할 댓글 내용
     * @return ResponseEntity<Void>
     * @author uyh
     * @created 2025-09-29
     * 본인이 작성한 댓글을 수정
     */
    @Operation(summary = "댓글 수정", description = "본인이 작성한 댓글을 수정합니다.")
    @PutMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable("commentId") Integer commentId,
            Principal principal,
            @RequestBody CommentRequest req) {
        String userId = principal.getName();
        commentService.updateComment(userId, commentId, req.getContent());
        return ResponseEntity.ok().build();
    }

    /**
     * @param commentId 삭제할 댓글 ID
     * @param principal 인증된 사용자 정보
     * @return ResponseEntity<Void>
     * @author uyh
     * @created 2025-09-29
     * 본인이 작성한 댓글을 삭제
     */
    @Operation(summary = "댓글 삭제", description = "본인이 작성한 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("commentId") Integer commentId,
            Principal principal) {
        String userId = principal.getName();
        commentService.deleteComment(userId, commentId);
        return ResponseEntity.noContent().build();
    }

    /**
     * @param reviewId 조회할 리뷰 ID
     * @return List<CommentResponse>
     * @author uyh
     * @created 2025-09-29
     * @updated 2025-10-12
     * 특정 리뷰의 모든 댓글을 조회 (isOwner 필드 추가)
     */
    @Operation(summary = "특정 리뷰의 댓글 조회", description = "리뷰 ID로 댓글을 조회합니다.")
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByReview(
            @PathVariable("reviewId") Integer reviewId,
            Principal principal) {
        String userId = principal != null ? principal.getName() : null;
        return ResponseEntity.ok(commentService.getCommentsByReview(reviewId, userId));
    }

    /**
     * @param principal 인증된 사용자 정보
     * @return ResponseEntity<List<CommentResponse>>
     * @author uyh
     * @created 2025-09-29
     * 본인이 작성한 모든 댓글을 조회
     */
    @Operation(summary = "내 댓글 전체 조회", description = "본인이 작성한 모든 댓글을 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<List<CommentResponse>> getCommentsByUser(Principal principal) {
        String userId = principal.getName();
        return ResponseEntity.ok(commentService.getCommentsByUser(userId));
    }
}