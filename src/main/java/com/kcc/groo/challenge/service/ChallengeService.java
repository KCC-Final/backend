package com.kcc.groo.challenge.service;

import com.kcc.groo.bookshelf.dao.IBookScrapRepository;
import com.kcc.groo.challenge.dao.IBadgeRepository;
import com.kcc.groo.challenge.data.dto.UserBadgeResponse;
import com.kcc.groo.challenge.data.dto.UserBadgeStatusResponse;
import com.kcc.groo.challenge.data.model.Badge;
import com.kcc.groo.review.dao.ICommentRepository;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService implements IChallengeService {

    private final IBadgeRepository badgeRepository;
    private final IUsersRepository usersRepository;
    private final IFollowsRepository followsRepository;
    private final IReviewRepository reviewRepository;
    private final ICommentRepository commentRepository;
    private final IBookScrapRepository bookScrapRepository;


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

    @Override
    public void checkReviewRelatedBadges(String userId) {
        checkAndAwardBadges(userId);
    }

    @Override
    @Transactional
    public void checkPioneerBadge(String userId, String isbn) {
        try {
            // 1. 해당 ISBN으로 작성된 리뷰 수 확인
            int reviewCount = reviewRepository.countReviewsByIsbn(isbn);

            // 2. 리뷰 수가 1개일 경우 (방금 작성된 리뷰가 첫 리뷰)
            if (reviewCount == 1) {
                // 3. '개척자' 뱃지 정보 조회
                Badge pioneerBadge = badgeRepository.findBadgeByName("개척자");
                if (pioneerBadge != null) {
                    // 4. 뱃지 부여
                    badgeRepository.awardBadgeToUser(userId, pioneerBadge.getBadgeId());
                    log.info("User {} achieved a new badge: {}", userId, pioneerBadge.getBadgeName());
                }
            }
        } catch (Exception e) {
            log.error("Error checking pioneer badge for userId: {} and isbn: {}", userId, isbn, e);
        }
    }

    @Override
    public List<UserBadgeResponse> getBadgesByUserId(String userId) {
        log.info("Getting badges for user: {}", userId);
        return badgeRepository.findBadgesByUserId(userId);
    }

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
                        // Calculate progress for unacquired badges
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
                                    // '개척자'는 리뷰 작성 시점에만 판별 가능하므로, 이 API에서는 진행도를 0으로 표시
                                    currentProgress = 0;
                                    break;
                                default:
                                    currentProgress = 0;
                                    break;
                            }
                        } catch (Exception e) {
                            log.error("Error calculating progress for badgeId: {} and userId: {}", badge.getBadgeId(), userId, e);
                            currentProgress = 0; // Set to 0 in case of error
                        }
                    }

                    return UserBadgeStatusResponse.from(badge, currentProgress, isAcquired, acquiredDate);
                })
                .collect(Collectors.toList());
    }
}