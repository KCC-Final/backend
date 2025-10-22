package com.kcc.groo.group.controller;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.group.service.IGroupScrapService;
import com.kcc.groo.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 독서모임 스크랩 관련 API를 처리하는 컨트롤러
 *
 * @author YunSung
 * @created 2025-10-23
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups/scrap")
public class GroupScrapController {

    private final IGroupScrapService groupScrapService;

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 독서 모임 게시글 스크랩
     *
     * @param groupId 스크랩할 독서 모임 ID
     * @param request HTTP 요청 객체
     * @return 응답 엔티티
     * @author YunSung
     * @created 2025-10-23
     */
    @PostMapping("/{groupId}")
    public ResponseEntity<CommonResponse<?>> createScrap(@PathVariable int groupId, HttpServletRequest request) {
        // JWT 토큰에서 사용자 ID 추출
        String userId = jwtTokenProvider.getUserId(jwtTokenProvider.resolveAccessToken(request));

        // 독서 모임 게시글 스크랩
        groupScrapService.scrapGroup(groupId, userId);

        // 201 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>(groupId + "번 독서 모임 게시글 스크랩에 성공했습니다", null));
    }

    /**
     * 독서 모임 게시글 스크랩 상태 조회
     *
     * @param groupId 스크랩 상태를 조회할 독서 모임 ID
     * @param request HTTP 요청 객체
     * @return 응답 엔티티
     * @author YunSung
     * @created 2025-10-23
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<CommonResponse<?>> getScrapStatus(@PathVariable int groupId, HttpServletRequest request) {
        // JWT 토큰에서 사용자 ID 추출
        String userId = jwtTokenProvider.getUserId(jwtTokenProvider.resolveAccessToken(request));

        // 독서 모임 게시글 스크랩 상태 조회
        boolean isScrapped = groupScrapService.isGroupScrappedByUser(groupId, userId);

        // 200 응답 반환
        return ResponseEntity.ok(new CommonResponse<>(groupId + "번 독서 모임 게시글 스크랩 상태 조회에 성공했습니다", isScrapped));
    }

    /**
     * 독서 모임 게시글 스크랩 취소
     *
     * @param groupId 스크랩 취소할 독서 모임 ID
     * @param request HTTP 요청 객체
     * @return 응답 엔티티
     * @author YunSung
     * @created 2025-10-23
     */
    @DeleteMapping("/{groupId}")
    public ResponseEntity<CommonResponse<?>> deleteScrap(@PathVariable int groupId, HttpServletRequest request) {
        // JWT 토큰에서 사용자 ID 추출
        String userId = jwtTokenProvider.getUserId(jwtTokenProvider.resolveAccessToken(request));

        // 독서 모임 게시글 스크랩 삭제
        groupScrapService.cancelScrapGroup(groupId, userId);

        // 200 응답 반환
        return ResponseEntity.ok(new CommonResponse<>(groupId + "번 독서 모임 게시글 스크랩 취소에 성공했습니다", null));
    }
}
