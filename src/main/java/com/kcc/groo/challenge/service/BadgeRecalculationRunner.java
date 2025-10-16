package com.kcc.groo.challenge.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BadgeRecalculationRunner implements CommandLineRunner {

    private final IChallengeService challengeService;

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
