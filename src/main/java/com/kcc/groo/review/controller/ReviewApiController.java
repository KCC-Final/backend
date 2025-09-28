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
     * TODO
     * 
     * @param @param principal
     * @param @param request
     * @param @return
     * @return ResponseEntity<Void>
     * @author kolgu
     * @created 2025. 9. 28. TODO
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
     * TODO
     * 
     * @param @param reviewId
     * @param @param principal
     * @param @param request
     * @param @return
     * @return ResponseEntity<Void>
     * @author kolgu
     * @created 2025. 9. 28. TODO
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
}
