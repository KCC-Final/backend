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

@Slf4j
@Component
@RequiredArgsConstructor
public class MonthlyBadgeScheduler {

    private final IReviewRepository reviewRepository;
    private final IBadgeRepository badgeRepository;

    /**
     * 매월 1일 새벽 2시에 실행되어, 지난달의 '이달의 독서왕'을 선정하고 뱃지를 부여합니다.
     * cron = "초 분 시 일 월 주"
     */
    @Scheduled(cron = "0 0 2 1 * *")
    @Transactional
    public void awardReaderOfMonthBadge() {
        log.info("Starting 'Reader of the Month' badge awarding scheduler.");

        try {
            // 1. '이달의 독서왕' 뱃지 정보 조회
            Badge readerOfMonthBadge = badgeRepository.findBadgeByName("이달의 독서왕");
            if (readerOfMonthBadge == null) {
                log.warn("'이달의 독서왕' 뱃지가 DB에 없습니다. 스케줄러를 중단합니다.");
                return;
            }

            // 2. 지난달 기간 계산
            YearMonth lastMonth = YearMonth.now().minusMonths(1);
            LocalDateTime startTime = lastMonth.atDay(1).atStartOfDay();
            LocalDateTime endTime = lastMonth.atEndOfMonth().atTime(23, 59, 59);

            log.info("Calculating 'Reader of the Month' for period: {} to {}", startTime, endTime);

            // 3. 지난달의 독서왕 후보 선정
            List<TopReviewerDto> topReviewers = reviewRepository.findTopReviewersByPeriod(startTime, endTime);

            if (topReviewers.isEmpty()) {
                log.info("No reviews were written last month. No 'Reader of the Month' will be awarded.");
                return;
            }

            // 4. 1등 점수를 기준으로 동점자 포함 모든 수상자 선정
            int topScore = topReviewers.get(0).getReviewCount();
            if (topScore == 0) {
                log.info("Top score is 0. No 'Reader of the Month' will be awarded.");
                return;
            }

            List<String> winners = topReviewers.stream()
                    .filter(reviewer -> reviewer.getReviewCount() == topScore)
                    .map(TopReviewerDto::getUserId)
                    .collect(Collectors.toList());

            // 5. 모든 수상자에게 뱃지 수여
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
