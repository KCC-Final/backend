package com.kcc.groo.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kcc.groo.challenge.dao.IBadgeRepository;
import com.kcc.groo.review.dao.IReviewRepository;
import com.kcc.groo.user.dao.IUsersRepository;

@Component
public class NotificationFormatter {

    @Autowired private IReviewRepository reviewRepository;
    @Autowired private IBadgeRepository badgeRepository;
    @Autowired private IUsersRepository usersRepository;

    /**
     * @param type
     * @param senderUserId
     * @param senderType
     * @param senderId
     * @param detailSenderId
     * @param userId
     * @return
     * @author kys
     * @created 2025-10-21
     * 알림 유형에 따른 문구 지정
     */
    public String generateContent(String type, String senderUserId, String senderType, int senderId, int detailSenderId, String userId) {
        String senderNickname = usersRepository.selectUserByUserId(senderUserId).getNickname();

        switch (type) {
            case "comment":
                String reviewTitle = reviewRepository.getReviewTitleByReviewId(senderId);
                return String.format("%s님이 [%s] 게시물에 댓글을 남겼습니다.", senderNickname, reviewTitle);

            case "like":
                String likedTitle = reviewRepository.getReviewTitleByReviewId(senderId);
                return String.format("%s님이 [%s] 게시물을 좋아합니다.", senderNickname, likedTitle);

            case "follow":
                return String.format("%s님이 %s님을 팔로우하기 시작했습니다.", senderNickname, userId);

            case "badge":
                String badgeName = badgeRepository.getBadgeNameByBadgeId(detailSenderId);
                return String.format("축하합니다! [%s] 뱃지를 획득했습니다", badgeName);

            default:
                return "새로운 알림이 있습니다.";
        }
    }
}