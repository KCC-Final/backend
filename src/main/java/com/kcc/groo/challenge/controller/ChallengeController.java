package com.kcc.groo.challenge.controller;

import com.kcc.groo.challenge.data.dto.UserBadgeResponse;
import com.kcc.groo.challenge.data.dto.UserBadgeStatusResponse;
import com.kcc.groo.challenge.service.IChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 도전과제 및 뱃지 관련 API 컨트롤러
 * @author uyh
 */
@Tag(name = "Challenge API", description = "사용자 뱃지 조회 및 진행도 확인 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/challenges")
public class ChallengeController {

    private final IChallengeService challengeService;

    /**
     * @param userId 조회할 사용자 ID
     * @return ResponseEntity<List<UserBadgeResponse>>
     * @author uyh
     * @created 2025-10-16
     * 특정 사용자의 뱃지 획득 목록을 조회
     */
    @Operation(summary = "특정 사용자 뱃지 목록 조회", description = "특정 사용자가 획득한 뱃지 목록을 획득일 순서대로 조회합니다.")
    @GetMapping("/users/{userId}/badges")
    public ResponseEntity<List<UserBadgeResponse>> getUserBadges(@PathVariable String userId) {
        List<UserBadgeResponse> badges = challengeService.getBadgesByUserId(userId);
        return ResponseEntity.ok(badges);
    }

    /**
     * @param userId 조회할 사용자 ID
     * @return ResponseEntity<List<UserBadgeStatusResponse>>
     * @author uyh
     * @created 2025-10-16
     * 특정 사용자의 전체 뱃지 목록과 획득 상태 및 진행도를 함께 조회
     */
    @Operation(summary = "특정 사용자의 전체 뱃지 목록과 획득 상태/진행도 조회", description = "전체 뱃지 목록을 조회하고, 사용자가 획득한 뱃지인지 여부, 획득 날짜, 현재 진행도를 함께 반환합니다.")
    @GetMapping("/users/{userId}/badges/all")
    public ResponseEntity<List<UserBadgeStatusResponse>> getAllBadgesWithUserStatus(@PathVariable String userId) {
        List<UserBadgeStatusResponse> badges = challengeService.getAllBadgesWithUserStatus(userId);
        return ResponseEntity.ok(badges);
    }
}
