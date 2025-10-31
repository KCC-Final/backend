package com.kcc.groo.test.controller;

import com.kcc.groo.challenge.service.MonthlyBadgeScheduler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test API", description = "개발 및 테스트용 API")
@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    private final MonthlyBadgeScheduler monthlyBadgeScheduler;

    @GetMapping("/api/test/ping")
    public String ping() {
        return "pong from backend 🚀";
    }

    @Operation(summary = "수동으로 '이달의 독서왕' 스케줄러 실행", description = "테스트 목적으로 '이달의 독서왕' 뱃지를 부여하는 월간 스케줄러를 즉시 실행합니다.")
    @PostMapping("/api/test/run-monthly-scheduler")
    public ResponseEntity<String> runMonthlyScheduler() {
        // 스케줄러의 public 메소드를 직접 호출
        monthlyBadgeScheduler.assignMonthlyKingBadge();
        return ResponseEntity.ok("Monthly badge scheduler has been triggered successfully. Check the server logs for details.");
    }
}
