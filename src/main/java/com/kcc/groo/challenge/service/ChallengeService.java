package com.kcc.groo.challenge.service;

import com.kcc.groo.bookshelf.dao.IBookScrapRepository;
import com.kcc.groo.challenge.dao.IBadgeRepository;
import com.kcc.groo.challenge.data.dto.UserBadgeResponse;
import com.kcc.groo.challenge.data.dto.UserBadgeStatusResponse;
import com.kcc.groo.challenge.data.model.Badge;
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

    /**
     * @param userId 사용자 ID
     * @return void
     * @author uyh
     * @created 2025-10-16
     * 사용자의 활동 기록을 확인하고 달성 가능한 뱃지를 자동으로 부여
     */
    @Override
    @Transactional
    public void checkAndAwardBadges(String userId) {
        log.info("Checking badges for user: {}", userId);
        List<Badge> allBadges = badgeRepository.findAllBadges();
        Set<Integer> userBadgeIds = Set.copyOf(badgeRepository.findBadgeIdsByUserId(userId));

        for (Badge badge : allBadges) {
            if (userBadgeIds.contains(badge.getBadgeId())) {
                continue;
            }
            boolean achieved = false;
            try {
                switch (badge.getBadgeName()) {
                    // Review Count Badges
                    case "첫 발자국":
                    case "독서가":
                    case "애독가":
                    case "열혈 독서가":
                    case "책의 지배자":
                        int reviewCount = badgeRepository.countReviewsByUserId(userId);
                        if (reviewCount >= badge.getBadgeConditions()) {
                            achieved = true;
                        }
                        break;
                    // Single Category Review Count Badges
                    case "한 우물 파기 I":
                    case "한 우물 파기 II":
                    case "한 우물 파기 III":
                    case "한 우물 파기 IV":
                        Integer maxReviewInSingleCategory = badgeRepository.getMaxReviewCountForSingleCategory(userId);
                        if (maxReviewInSingleCategory != null && maxReviewInSingleCategory >= badge.getBadgeConditions()) {
                            achieved = true;
                        }
                        break;
                    // Multiple Categories Review Count Badges
                    case "작은 탐험가":
                    case "넓은 탐험가":
                    case "위대한 탐험가":
                        int categoryCount = badgeRepository.getCategoryCountWithMinReviews(userId, badge.getBadgeConditions());
                        if (categoryCount >= badge.getBadgeConditions()) {
                            achieved = true;
                        }
                        break;
                    // First Activity Badges
                    case "첫 공감":
                        if (reviewRepository.countLikesByUserId(userId) >= badge.getBadgeConditions()) {
                            achieved = true;
                        }
                        break;
                    case "첫 소통":
                        if (commentRepository.countCommentsByUserId(userId) >= badge.getBadgeConditions()) {
                            achieved = true;
                        }
                        break;
                    case "첫 인연":
                        if (followsRepository.countFollowing(userId) >= badge.getBadgeConditions()) {
                            achieved = true;
                        }
                        break;
                    case "첫 발견":
                        if (bookScrapRepository.countTotalBookScrapByUserId(userId) >= badge.getBadgeConditions()) {
                            achieved = true;
                        }
                        break;
                    case "첫인사":
                        Users user = usersRepository.selectUserByUserId(userId);
                        if (user != null && StringUtils.hasText(user.getIntroduction())) {
                            achieved = true;
                        }
                        break;
                }
            } catch (Exception e) {
                log.error("Error checking badge condition for badgeId: {} and userId: {}", badge.getBadgeId(), userId, e);
            }
            if (achieved) {
                log.info("User {} achieved a new badge: {}", userId, badge.getBadgeName());
                badgeRepository.awardBadgeToUser(userId, badge.getBadgeId());
            }
        }
    }

    /**
     * @return void
     * @author uyh
     * @created 2025-10-16
     * 모든 사용자의 뱃지를 재계산하여 부여
     */
    @Override
    @Transactional
    public void recalculateAllUsersBadges() {
        log.info("Starting badge recalculation for all users.");
        List<String> allUserIds = usersRepository.findAllUserIds();
        log.info("Found {} users to recalculate.", allUserIds.size());
        for (String userId : allUserIds) {
            try {
                checkAndAwardBadges(userId);
            } catch (Exception e) {
                log.error("Error recalculating badges for user: {}", userId, e);
            }
        }
        log.info("Finished badge recalculation for all users.");
    }

    /**
     * @param userId 사용자 ID
     * @return void
     * @author uyh
     * @created 2025-10-16
     * 리뷰 작성과 관련된 뱃지 조건을 확인하고 부여
     */
    @Override
    public void checkReviewRelatedBadges(String userId) {
        checkAndAwardBadges(userId);
    }

    /**
     * @param userId 사용자 ID
     * @param isbn   도서 ISBN
     * @return void
     * @author uyh
     * @created 2025-10-16
     * 개척자 뱃지 (새로운 도서에 대한 첫 독후감) 달성 여부를 확인하고 뱃지를 부여
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
                    log.info("User {} achieved a new badge: {}", userId, pioneerBadge.getBadgeName());
                }
            }
        } catch (Exception e) {
            log.error("Error checking pioneer badge for userId: {} and isbn: {}", userId, isbn, e);
        }
    }

    /**
     * @param userId 사용자 ID
     * @return List<UserBadgeResponse>
     * @author uyh
     * @created 2025-10-16
     * 특정 사용자의 뱃지 획득 목록 조회
     */
    @Override
    public List<UserBadgeResponse> getBadgesByUserId(String userId) {
        log.info("Getting badges for user: {}", userId);
        return badgeRepository.findBadgesByUserId(userId);
    }

    /**
     * @param userId 사용자 ID
     * @return List<UserBadgeStatusResponse>
     * @author uyh
     * @created 2025-10-16
     * 특정 사용자의 전체 뱃지 목록과 획득 상태를 함께 조회
     */
    @Override
    public List<UserBadgeStatusResponse> getAllBadgesWithUserStatus(String userId) {
        log.info("Getting all badges with user status for user: {}", userId);

        List<Badge> allBadges = badgeRepository.findAllBadges();
        List<UserBadgeResponse> acquiredBadgesList = getBadgesByUserId(userId);
        Map<Integer, UserBadgeResponse> acquiredBadgesMap = acquiredBadgesList.stream()
                .collect(Collectors.toMap(UserBadgeResponse::getBadgeId, Function.identity()));

        return allBadges.stream()
                .map(badge -> {
                    UserBadgeResponse acquiredBadge = acquiredBadgesMap.get(badge.getBadgeId());
                    boolean isAcquired = acquiredBadge != null;
                    LocalDateTime acquiredDate = isAcquired ? acquiredBadge.getSucceededAt() : null;

                    int currentProgress = 0;
                    if (isAcquired) {
                        currentProgress = badge.getBadgeConditions();
                    } else {
                        try {
                            switch (badge.getBadgeName()) {
                                case "첫 발자국":
                                case "독서가":
                                case "애독가":
                                case "열혈 독서가":
                                case "책의 지배자":
                                    currentProgress = badgeRepository.countReviewsByUserId(userId);
                                    break;
                                case "한 우물 파기 I":
                                case "한 우물 파기 II":
                                case "한 우물 파기 III":
                                case "한 우물 파기 IV":
                                    Integer maxReview = badgeRepository.getMaxReviewCountForSingleCategory(userId);
                                    currentProgress = (maxReview != null) ? maxReview : 0;
                                    break;
                                case "작은 탐험가":
                                case "넓은 탐험가":
                                case "위대한 탐험가":
                                    currentProgress = badgeRepository.getCategoryCountWithMinReviews(userId, badge.getBadgeConditions());
                                    break;
                                case "첫 공감":
                                    currentProgress = reviewRepository.countLikesByUserId(userId) > 0 ? 1 : 0;
                                    break;
                                case "첫 소통":
                                    currentProgress = commentRepository.countCommentsByUserId(userId) > 0 ? 1 : 0;
                                    break;
                                case "첫 인연":
                                    currentProgress = followsRepository.countFollowing(userId) > 0 ? 1 : 0;
                                    break;
                                case "첫 발견":
                                    currentProgress = bookScrapRepository.countTotalBookScrapByUserId(userId) > 0 ? 1 : 0;
                                    break;
                                case "첫인사":
                                    Users user = usersRepository.selectUserByUserId(userId);
                                    currentProgress = (user != null && StringUtils.hasText(user.getIntroduction())) ? 1 : 0;
                                    break;
                                case "개척자":
                                    currentProgress = 0;
                                    break;
                                default:
                                    currentProgress = 0;
                                    break;
                            }
                        } catch (Exception e) {
                            log.error("Error calculating progress for badgeId: {} and userId: {}", badge.getBadgeId(), userId, e);
                            currentProgress = 0;
                        }
                    }

                    return UserBadgeStatusResponse.from(badge, currentProgress, isAcquired, acquiredDate);
                })
                .collect(Collectors.toList());
    }
}