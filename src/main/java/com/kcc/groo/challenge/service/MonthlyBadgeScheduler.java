package com.kcc.groo.challenge.service;

import com.kcc.groo.challenge.dao.IBadgeRepository;
import com.kcc.groo.challenge.data.model.Badge;
import com.kcc.groo.review.dao.IReviewRepository;
import com.kcc.groo.review.data.dto.TopReviewerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 매월 1일 새벽 2시에 실행되어 이달의 독서왕 뱃지를 부여하는 스케줄러 클래스
 * @author uyh
 * @created 2025-10-16
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MonthlyBadgeScheduler {

    private final IReviewRepository reviewRepository;
    private final IBadgeRepository badgeRepository;

    /**
     * @return void
     * @author uyh
     * @created 2025-10-16
     * 매월 1일 새벽 2시에 실행되어 지난달의 이달의 독서왕을 선정하고 뱃지를 부여
     */
    @Scheduled(cron = "0 0 2 1 * *")
    @Transactional
    public void awardReaderOfMonthBadge() {
        log.info("Starting 'Reader of the Month' badge awarding scheduler.");

        try {
            Badge readerOfMonthBadge = badgeRepository.findBadgeByName("이달의 독서왕");
            if (readerOfMonthBadge == null) {
                log.warn("'이달의 독서왕' 뱃지가 DB에 없습니다. 스케줄러를 중단합니다.");
                return;
            }

            YearMonth lastMonth = YearMonth.now().minusMonths(1);
            LocalDateTime startTime = lastMonth.atDay(1).atStartOfDay();
            LocalDateTime endTime = lastMonth.atEndOfMonth().atTime(23, 59, 59);

            log.info("Calculating 'Reader of the Month' for period: {} to {}", startTime, endTime);

            List<TopReviewerDto> topReviewers = reviewRepository.findTopReviewersByPeriod(startTime, endTime);

            if (topReviewers.isEmpty()) {
                log.info("No reviews were written last month. No 'Reader of the Month' will be awarded.");
                return;
            }

            int topScore = topReviewers.get(0).getReviewCount();
            if (topScore == 0) {
                log.info("Top score is 0. No 'Reader of the Month' will be awarded.");
                return;
            }

            List<String> winners = topReviewers.stream()
                    .filter(reviewer -> reviewer.getReviewCount() == topScore)
                    .map(TopReviewerDto::getUserId)
                    .collect(Collectors.toList());

            log.info("Awarding 'Reader of the Month' badge to {} winner(s): {}", winners.size(), winners);
            for (String winnerId : winners) {
                badgeRepository.awardBadgeToUser(winnerId, readerOfMonthBadge.getBadgeId());
                log.info("Successfully awarded badge to user: {}", winnerId);
            }

        } catch (Exception e) {
            log.error("An error occurred during 'Reader of the Month' badge awarding.", e);
        }

        log.info("Finished 'Reader of the Month' badge awarding scheduler.");
    }
}