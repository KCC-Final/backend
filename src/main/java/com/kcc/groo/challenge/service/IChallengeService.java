package com.kcc.groo.challenge.service;

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
}