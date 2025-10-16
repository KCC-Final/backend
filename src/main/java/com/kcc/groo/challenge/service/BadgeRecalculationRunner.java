package com.kcc.groo.challenge.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 애플리케이션 시작 시 모든 사용자의 뱃지를 재계산하는 Runner 클래스
 * @author uyh
 * @created 2025-10-16
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BadgeRecalculationRunner implements CommandLineRunner {

    private final IChallengeService challengeService;

    /**
     * @param args 커맨드 라인 인자
     * @return void
     * @author uyh
     * @created 2025-10-16
     * 애플리케이션 시작 시 모든 사용자의 뱃지를 재계산
     */
    @Override
    public void run(String... args) throws Exception {
        log.info("Starting badge recalculation for all users on application startup.");
        try {
            challengeService.recalculateAllUsersBadges();
            log.info("Finished badge recalculation for all users.");
        } catch (Exception e) {
            log.error("An error occurred during badge recalculation on startup.", e);
        }
    }
}