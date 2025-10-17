package com.kcc.groo.challenge.service;

import com.kcc.groo.challenge.data.dto.UserBadgeResponse;
import com.kcc.groo.challenge.data.dto.UserBadgeStatusResponse;

import java.util.List;

/**
 * 사용자의 활동에 따라 도전과제를 확인하고 뱃지를 부여하는 서비스 인터페이스.
 * @author uyh
 */
public interface IChallengeService {

    void checkAndAwardBadges(String userId);

    void recalculateAllUsersBadges();

    void checkReviewRelatedBadges(String userId);

    /**
     * 개척자 뱃지 (새로운 도서에 대한 첫 독후감) 달성 여부를 확인하고 뱃지를 부여합니다.
     * @param userId 사용자 ID
     * @param isbn 도서 ISBN
     * @author uyh
     */
    void checkPioneerBadge(String userId, String isbn);

    /**
     * 특정 사용자의 뱃지 획득 목록 조회
     * @param userId 사용자 ID
     * @return 획득한 뱃지 상세 정보 목록
     * @author uyh
     */
    List<UserBadgeResponse> getBadgesByUserId(String userId);

    /**
     * 특정 사용자의 전체 뱃지 목록과 획득 상태를 함께 조회
     * @param userId 사용자 ID
     * @return 전체 뱃지 정보 및 획득 상태 목록
     * @author uyh
     */
    List<UserBadgeStatusResponse> getAllBadgesWithUserStatus(String userId);
}