package com.kcc.groo.review.controller;

import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.review.data.dto.*;
import com.kcc.groo.review.service.IReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Tag(name = "Review API", description = "독후감 CRUD + 임시저장 API")
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewApiController {

    private final IReviewService reviewService;
    private final JwtTokenProvider jwtTokenProvider;

    // --- 독후감 작성 ---
    /**
     * TODO
     * 
     * @param @param request
     * @param @param req
     * @param @return
     * @return ResponseEntity<Void>
     * @author kolgu
     * @created 2025. 9. 28. TODO
     */
    @Operation(summary = "독후감 작성", description = "JWT 토큰에서 추출한 userId 기준으로 독후감을 작성합니다.")
    @PostMapping
    public ResponseEntity<Void> createReview(HttpServletRequest request,
                                             @RequestBody ReviewCreateRequest req) {
        String token = jwtTokenProvider.resolveToken(request);
        String userId = jwtTokenProvider.getUserId(token);
        reviewService.createReview(userId, req);
        return ResponseEntity.ok().build();
    }
}
