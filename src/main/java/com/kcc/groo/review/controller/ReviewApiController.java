package com.kcc.groo.review.controller;

import com.kcc.groo.review.data.dto.*;
import com.kcc.groo.review.service.IReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "Review API", description = "독후감 CRUD + 임시저장 + 좋아요 API")
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewApiController {

    private final IReviewService reviewService;

    /**
     * @param principal 인증된 사용자 정보
     * @param request 독후감 작성 정보
     * @return ResponseEntity<Void>
     * @author uyh
     * @created 2025-09-28
     * JWT 토큰에서 추출한 userId 기준으로 독후감을 작성
     */
    @Operation(summary = "독후감 작성", description = "JWT 토큰에서 추출한 userId 기준으로 독후감을 작성합니다.")
    @PostMapping
    public ResponseEntity<Void> createReview(
            Principal principal,
            @RequestBody ReviewCreateRequest request) {
        String userId = principal.getName();
        reviewService.createReview(userId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * @param principal 인증된 사용자 정보
     * @return ResponseEntity<List<ReviewResponse>>
     * @author uyh
     * @created 2025-09-28
     * 로그인한 사용자의 임시저장 글 목록을 조회
     */
    @Operation(summary = "임시저장 목록 조회", description = "로그인한 사용자의 임시저장 글 목록을 조회합니다.")
    @GetMapping("/drafts")
    public ResponseEntity<List<ReviewResponse>> getDrafts(Principal principal) {
        String userId = principal.getName();
        return ResponseEntity.ok(reviewService.getDrafts(userId));
    }

    /**
     * @param id 조회할 임시저장 글 ID
     * @param principal 인증된 사용자 정보
     * @return ResponseEntity<ReviewResponse>
     * @author uyh
     * @created 2025-09-28
     * 특정 임시저장 글을 조회
     */
    @Operation(summary = "임시저장 글 단건 조회", description = "특정 임시저장 글을 조회합니다.")
    @GetMapping("/drafts/{id}")
    public ResponseEntity<ReviewResponse> getDraft(
            @PathVariable("id") int id,
            Principal principal) {
        String userId = principal.getName();
        return ResponseEntity.ok(reviewService.getDraft(id, userId));
    }

    /**
     * @param id 삭제할 임시저장 글 ID
     * @param principal 인증된 사용자 정보
     * @return ResponseEntity<Void>
     * @author uyh
     * @created 2025-09-28
     * 본인이 작성한 임시저장 글을 삭제
     */
    @Operation(summary = "임시저장 글 삭제", description = "본인이 작성한 임시저장 글을 삭제합니다.")
    @DeleteMapping("/drafts/{id}")
    public ResponseEntity<Void> deleteDraft(
            @PathVariable("id") int id,
            Principal principal) {
        String userId = principal.getName();
        reviewService.deleteDraft(id, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * @param reviewId 조회할 리뷰 ID
     * @param principal 인증된 사용자 정보
     * @return ResponseEntity<ReviewResponse>
     * @author uyh
     * @created 2025-09-29
     * 리뷰 ID로 독후감을 조회, 비밀글은 작성자 본인만 조회 가능
     */
    @Operation(summary = "독후감 단건 조회", description = "리뷰 ID로 독후감을 조회합니다. 비밀글은 작성자 본인만 조회할 수 있습니다.")
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(
            @PathVariable("reviewId") Integer reviewId,
            Principal principal) {
        String userId = principal != null ? principal.getName() : null;
        return ResponseEntity.ok(reviewService.getReview(userId, reviewId));
    }

    /**
     * @param principal 인증된 사용자 정보
     * @return ResponseEntity<List<ReviewResponse>>
     * @author uyh
     * @created 2025-09-28
     * 모든 공개 독후감을 조회, 본인의 비밀글은 본인만 조회 가능
     */
    @Operation(summary = "독후감 전체 조회", description = "모든 공개 독후감을 조회합니다. 본인의 비밀글은 본인만 조회할 수 있습니다.")
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReviews(Principal principal) {
        String userId = principal != null ? principal.getName() : null;
        return ResponseEntity.ok(reviewService.getAllReviews(userId));
    }

    /**
     * @param reviewId 수정할 리뷰 ID
     * @param principal 인증된 사용자 정보
     * @param request 수정할 독후감 정보
     * @return ResponseEntity<Void>
     * @author uyh
     * @created 2025-09-28
     * 본인이 작성한 독후감을 수정
     */
    @Operation(summary = "독후감 수정", description = "본인이 작성한 독후감을 수정합니다.")
    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(
            @PathVariable("reviewId") Integer reviewId,
            Principal principal,
            @RequestBody ReviewUpdateRequest request) {
        String userId = principal.getName();
        reviewService.updateReview(userId, reviewId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * @param principal 인증된 사용자 정보
     * @return ResponseEntity<List<ReviewResponse>>
     * @author uyh
     * @created 2025-09-29
     * 로그인한 사용자가 작성한 모든 리뷰를 조회
     */
    @Operation(summary = "내가 작성한 리뷰 전체 조회", description = "로그인한 사용자가 작성한 모든 리뷰를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(Principal principal) {
        String userId = principal.getName();
        return ResponseEntity.ok(reviewService.getReviewsByUser(userId));
    }

    /**
     * @param reviewId 삭제할 리뷰 ID
     * @param principal 인증된 사용자 정보
     * @return ResponseEntity<Void>
     * @author uyh
     * @created 2025-09-28
     * 본인이 작성한 독후감을 삭제
     */
    @Operation(summary = "독후감 삭제", description = "본인이 작성한 독후감을 삭제합니다.")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable("reviewId") Integer reviewId,
            Principal principal) {
        String userId = principal.getName();
        reviewService.deleteReview(userId, reviewId);
        return ResponseEntity.noContent().build();
    }

    /**
     * @param reviewId 좋아요할 리뷰 ID
     * @param principal 인증된 사용자 정보
     * @return ResponseEntity<Void>
     * @author uyh
     * @created 2025-09-28
     * 리뷰에 좋아요를 추가
     */
    @Operation(summary = "독후감 좋아요", description = "리뷰에 좋아요를 추가합니다.")
    @PostMapping("/{reviewId}/like")
    public ResponseEntity<Void> likeReview(
            @PathVariable("reviewId") Integer reviewId,
            Principal principal) {
        String userId = principal.getName();
        reviewService.likeReview(userId, reviewId);
        return ResponseEntity.ok().build();
    }

    /**
     * @param reviewId 좋아요 취소할 리뷰 ID
     * @param principal 인증된 사용자 정보
     * @return ResponseEntity<Void>
     * @author uyh
     * @created 2025-09-28
     * 리뷰 좋아요를 취소
     */
    @Operation(summary = "독후감 좋아요 취소", description = "리뷰 좋아요를 취소합니다.")
    @DeleteMapping("/{reviewId}/like")
    public ResponseEntity<Void> unlikeReview(
            @PathVariable("reviewId") Integer reviewId,
            Principal principal) {
        String userId = principal.getName();
        reviewService.unlikeReview(userId, reviewId);
        return ResponseEntity.noContent().build();
    }

    /**
     * @param principal 인증된 사용자 정보
     * @return ResponseEntity<List<ReviewResponse>>
     * @author uyh
     * @created 2025-09-28
     * 본인이 좋아요한 모든 독후감을 조회
     */
    @Operation(summary = "좋아요한 독후감 목록", description = "본인이 좋아요한 모든 독후감을 조회합니다.")
    @GetMapping("/likes/me")
    public ResponseEntity<List<ReviewResponse>> getLikedReviews(Principal principal) {
        String userId = principal.getName();
        return ResponseEntity.ok(reviewService.getLikedReviews(userId));
    }
    
    /**
     * @param principal 인증된 사용자 정보
     * @return ResponseEntity<List<ReviewResponse>>
     * @author uyh
     * @created 2025-10-01
     * 팔로잉한 유저들의 독후감을 최신순으로 조회
     */
    @Operation(summary = "팔로잉 유저 독후감 최신순 조회", description = "팔로잉한 유저들의 독후감을 최신순으로 조회합니다.")
    @GetMapping("/following")
    public ResponseEntity<List<ReviewResponse>> getReviewsByFollowing(Principal principal) {
        String userId = principal.getName();
        return ResponseEntity.ok(reviewService.getReviewsByFollowing(userId));
    }
    
    /**
     * @param principal 인증된 사용자 정보
     * @return ResponseEntity<List<ReviewResponse>>
     * @author uyh
     * @created 2025-10-01
     * 전체 유저의 독후감을 1주일간 좋아요 많은 순으로 조회
     */
    @Operation(summary = "독후감 인기순 조회 (1주일)", description = "전체 유저의 독후감을 최근 1주일간 좋아요가 많은 순으로 조회합니다.")
    @GetMapping("/popular")
    public ResponseEntity<List<ReviewResponse>> getAllReviewsOrderByLikes(Principal principal) {
        String userId = principal != null ? principal.getName() : null;
        return ResponseEntity.ok(reviewService.getAllReviewsOrderByLikes(userId));
    }
}