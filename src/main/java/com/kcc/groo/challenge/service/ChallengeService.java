package com.kcc.groo.challenge.service;

import com.kcc.groo.bookshelf.dao.IBookScrapRepository;
import com.kcc.groo.challenge.dao.IBadgeRepository;
import com.kcc.groo.challenge.data.dto.UserBadgeResponse;
import com.kcc.groo.challenge.data.dto.UserBadgeStatusResponse;
import com.kcc.groo.challenge.data.model.Badge;
import com.kcc.groo.notification.data.dto.NotificationRequest;
import com.kcc.groo.notification.service.NotificationService;
import com.kcc.groo.review.dao.IReviewCommentRepository;
import com.kcc.groo.review.dao.IReviewRepository;
import com.kcc.groo.user.dao.IFollowsRepository;
import com.kcc.groo.user.dao.IUsersRepository;
import com.kcc.groo.user.data.model.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 도전과제 및 뱃지 부여 관련 비즈니스 로직을 처리하는 서비스 클래스
 *
 * @author uyh
 * @created 2025-10-16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService implements IChallengeService {

    private final IBadgeRepository badgeRepository;
    private final IUsersRepository usersRepository;
    private final IFollowsRepository followsRepository;
    private final IReviewRepository reviewRepository;
    private final IReviewCommentRepository commentRepository;
    private final IBookScrapRepository bookScrapRepository;
    private final NotificationService notificationService;

    /**
     * 사용자의 모든 활동 내역을 바탕으로 달성 가능한 뱃지를 자동으로 부여
     */
    @Override
    @Transactional
    public void checkAndAwardBadges(String userId) {
        log.info(" Checking all badge conditions for user: {}", userId);

        // 전체 뱃지와 사용자 보유 뱃지 목록 조회
        List<Badge> allBadges = badgeRepository.findAllBadges();
        Set<Integer> userBadgeIds = Set.copyOf(badgeRepository.findBadgeIdsByUserId(userId));

        // 사용자 정보와 활동량 미리 로드
        Users user = usersRepository.selectUserByUserId(userId);
        int reviewCount = badgeRepository.countReviewsByUserId(userId);
        Integer maxReviewInSingleCategory = badgeRepository.getMaxReviewCountForSingleCategory(userId);
        int likeCount = reviewRepository.countLikesByUserId(userId);
        int commentCount = commentRepository.countCommentsByUserId(userId);
        int followCount = followsRepository.countFollowing(userId);
        int scrapCount = bookScrapRepository.countTotalBookScrapByUserId(userId);
        boolean hasIntro = (user != null && StringUtils.hasText(user.getIntroduction()));

        for (Badge badge : allBadges) {
            if (userBadgeIds.contains(badge.getBadgeId())) continue; // 이미 획득한 뱃지 건너뛰기

            boolean achieved = false;
            String badgeName = badge.getBadgeName();

            try {
                // ===  리뷰 관련 뱃지 ===
                if (List.of("첫 발자국", "독서가", "애독가", "열혈 독서가", "책의 지배자").contains(badgeName)) {
                    achieved = reviewCount >= badge.getBadgeConditions();
                }

                // ===  카테고리 집중형 뱃지 ===
                else if (List.of("한 우물 파기 I", "한 우물 파기 II", "한 우물 파기 III", "한 우물 파기 IV").contains(badgeName)) {
                    achieved = (maxReviewInSingleCategory != null && maxReviewInSingleCategory >= badge.getBadgeConditions());
                }

                // ===  탐험가 계열 (다양한 카테고리) ===
                else if (List.of("작은 탐험가", "넓은 탐험가", "위대한 탐험가").contains(badgeName)) {
                    int categoryCount = badgeRepository.getCategoryCountWithMinReviews(userId, badge.getBadgeConditions());
                    achieved = categoryCount >= badge.getBadgeConditions();
                }

                // ===  첫 공감 (좋아요) ===
                else if ("첫 공감".equals(badgeName)) {
                    achieved = likeCount >= badge.getBadgeConditions();
                }

                // ===  첫 소통 (댓글) ===
                else if ("첫 소통".equals(badgeName)) {
                    achieved = commentCount >= badge.getBadgeConditions();
                }

                // ===  첫 인연 (팔로우) ===
                else if ("첫 인연".equals(badgeName)) {
                    achieved = followCount >= badge.getBadgeConditions();
                }

                // ===  첫 발견 (책 스크랩) ===
                else if ("첫 발견".equals(badgeName)) {
                    achieved = scrapCount >= badge.getBadgeConditions();
                }

                // ===  첫인사 (프로필 작성) ===
                else if ("첫인사".equals(badgeName)) {
                    achieved = hasIntro;
                }

                // ===  개척자 (새로운 도서의 첫 리뷰) ===
                else if ("개척자".equals(badgeName)) {
                    // 별도 checkPioneerBadge()에서만 지급
                    continue;
                }

            } catch (Exception e) {
                log.error(" Error evaluating badge '{}' for user {}: {}", badgeName, userId, e.getMessage());
            }

            // 뱃지 달성 시 처리
            if (achieved) {
                badgeRepository.awardBadgeToUser(userId, badge.getBadgeId());
                log.info(" User '{}' achieved new badge: {}", userId, badgeName);

                // 알림 발송
                NotificationRequest req = new NotificationRequest();
                req.setType("badge");
                req.setSenderType("users");
                req.setSenderId(badge.getBadgeId());
                req.setDetailSenderId(badge.getBadgeId());
                req.setUserId(userId);
                req.setSenderUserId(userId);
                notificationService.sendNotification(req);
            }
        }
    }


    /**
     * 개척자 뱃지 (새로운 도서의 첫 리뷰) 별도 처리
     */
    @Override
    @Transactional
    public void checkPioneerBadge(String userId, String isbn) {
        try {
            int reviewCount = reviewRepository.countReviewsByIsbn(isbn);
            if (reviewCount == 1) {
                Badge pioneerBadge = badgeRepository.findBadgeByName("개척자");
                if (pioneerBadge != null) {
                    badgeRepository.awardBadgeToUser(userId, pioneerBadge.getBadgeId());
                    log.info(" User '{}' achieved new badge: {}", userId, pioneerBadge.getBadgeName());

                    // 알림 발송
                    NotificationRequest req = new NotificationRequest();
                    req.setType("badge");
                    req.setSenderType("users");
                    req.setSenderId(pioneerBadge.getBadgeId());
                    req.setDetailSenderId(pioneerBadge.getBadgeId());
                    req.setUserId(userId);
                    req.setSenderUserId(userId);
                    notificationService.sendNotification(req);
                }
            }
        } catch (Exception e) {
            log.error("Error checking pioneer badge for user: {}", userId, e);
        }
    }

    /**
     * 모든 사용자에 대해 뱃지를 재계산 (배치/스케줄러용)
     */
    @Override
    @Transactional
    public void recalculateAllUsersBadges() {
        log.info("Starting global badge recalculation.");
        List<String> allUserIds = usersRepository.findAllUserIds();

        for (String userId : allUserIds) {
            try {
                checkAndAwardBadges(userId);
            } catch (Exception e) {
                log.error("Error recalculating badges for user {}: {}", userId, e.getMessage());
            }
        }
        log.info("Finished global badge recalculation.");
    }

    /**
     * 특정 사용자의 뱃지 획득 목록 조회
     */
    @Override
    public List<UserBadgeResponse> getBadgesByUserId(String userId) {
        return badgeRepository.findBadgesByUserId(userId);
    }

    /**
     * 전체 뱃지 + 진행도 조회
     */
    @Override
    public List<UserBadgeStatusResponse> getAllBadgesWithUserStatus(String userId) {
        List<Badge> allBadges = badgeRepository.findAllBadges();
        List<UserBadgeResponse> acquired = getBadgesByUserId(userId);
        Map<Integer, UserBadgeResponse> acquiredMap = acquired.stream()
                .collect(Collectors.toMap(UserBadgeResponse::getBadgeId, Function.identity()));

        return allBadges.stream().map(badge -> {
            UserBadgeResponse acquiredBadge = acquiredMap.get(badge.getBadgeId());
            boolean isAcquired = acquiredBadge != null;
            LocalDateTime acquiredDate = isAcquired ? acquiredBadge.getSucceededAt() : null;
            int progress = 0;

            try {
                if (isAcquired) {
                    progress = badge.getBadgeConditions();
                } else {
                    progress = calculateProgress(userId, badge);
                }
            } catch (Exception e) {
                log.error("Progress calc error for badge {}: {}", badge.getBadgeName(), e.getMessage());
            }

            return UserBadgeStatusResponse.from(badge, progress, isAcquired, acquiredDate);
        }).collect(Collectors.toList());
    }

    /**
     * 뱃지별 진행도 계산 로직 (UI용)
     */
    private int calculateProgress(String userId, Badge badge) {
        String name = badge.getBadgeName();

        return switch (name) {
            case "첫 발자국", "독서가", "애독가", "열혈 독서가", "책의 지배자" ->
                    badgeRepository.countReviewsByUserId(userId);
            case "한 우물 파기 I", "한 우물 파기 II", "한 우물 파기 III", "한 우물 파기 IV" -> {
                Integer maxReview = badgeRepository.getMaxReviewCountForSingleCategory(userId);
                yield maxReview != null ? maxReview : 0;
            }
            case "작은 탐험가", "넓은 탐험가", "위대한 탐험가" ->
                    badgeRepository.getCategoryCountWithMinReviews(userId, badge.getBadgeConditions());
            case "첫 공감" ->
                    reviewRepository.countLikesByUserId(userId) > 0 ? 1 : 0;
            case "첫 소통" ->
                    commentRepository.countCommentsByUserId(userId) > 0 ? 1 : 0;
            case "첫 인연" ->
                    followsRepository.countFollowing(userId) > 0 ? 1 : 0;
            case "첫 발견" ->
                    bookScrapRepository.countTotalBookScrapByUserId(userId) > 0 ? 1 : 0;
            case "첫인사" -> {
                Users user = usersRepository.selectUserByUserId(userId);
                yield (user != null && StringUtils.hasText(user.getIntroduction())) ? 1 : 0;
            }
            default -> 0;
        };
    }
}