package com.kcc.groo.challenge.service;

import com.kcc.groo.challenge.dao.IBadgeRepository;
import com.kcc.groo.challenge.data.model.Badge;
import com.kcc.groo.notification.data.dto.NotificationRequest;
import com.kcc.groo.notification.service.NotificationService;
import com.kcc.groo.review.dao.IReviewRepository;
import com.kcc.groo.user.dao.IUsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * 매월 1일 00시 10분에 실행되어
 * 지난달의 독서왕(리뷰 최다 작성자)에게 "이달의 독서왕" 배지를 부여한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MonthlyBadgeScheduler {

    private final IUsersRepository usersRepository;
    private final IReviewRepository reviewRepository;
    private final IBadgeRepository badgeRepository;
    private final NotificationService notificationService;

    /**
     * @return void
     * @author uyh
     * @created 2025-10-16
     * 매월 1일 새벽 2시에 실행되어 지난달의 이달의 독서왕을 선정하고 뱃지를 부여
     */
    @Scheduled(cron = "0 10 0 1 * *")
    @Transactional
    public void assignMonthlyKingBadge() {
        log.info(" [Scheduler] Monthly Badge Job Started");

        // 1️ 이번 달, 지난 달 계산
        YearMonth now = YearMonth.now();
        YearMonth lastMonth = now.minusMonths(1);
        LocalDate startDate = lastMonth.atDay(1);
        LocalDate endDate = lastMonth.atEndOfMonth();

        // 2 지난달 리뷰 최다 작성자 찾기
        String topUserId = reviewRepository.findTopReviewerByPeriod(startDate, endDate);
        if (topUserId == null) {
            log.info(" No top reviewer found for {}", lastMonth);
            return;
        }

        // 3️ "이달의 독서왕" 배지 조회
        Badge badge = badgeRepository.findBadgeByName("이달의 독서왕");
        if (badge == null) {
            log.warn(" Badge '이달의 독서왕' not found");
            return;
        }

        int badgeId = badge.getBadgeId();

        // 4️ 같은 달 중복 방지
        boolean alreadyAwarded = badgeRepository.existsMonthlyBadge(topUserId, badgeId, lastMonth.toString());
        if (alreadyAwarded) {
            log.info(" Already awarded monthly badge for {} to {}", lastMonth, topUserId);
            return;
        }

        // 5️ 배지 부여
        int result = badgeRepository.awardBadgeToUser(topUserId, badgeId);
        if (result > 0) {
            log.info(" '{}' awarded '{}' for {}", topUserId, badge.getBadgeName(), lastMonth);

            NotificationRequest req = new NotificationRequest();
            req.setType("badge");
            req.setSenderType("system");
            req.setSenderId(badgeId);
            req.setDetailSenderId(badgeId);
            req.setUserId(topUserId);
            req.setSenderUserId("system");
            notificationService.sendNotification(req);
        } else {
            log.warn(" Failed to insert monthly badge for {}", topUserId);
        }

        log.info(" [Scheduler] Monthly Badge Job Completed");
    }
}
