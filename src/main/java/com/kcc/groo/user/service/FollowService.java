package com.kcc.groo.user.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kcc.groo.challenge.service.IChallengeService; // 추가
import com.kcc.groo.notification.data.dto.NotificationRequest;
import com.kcc.groo.notification.service.INotificationService;
import com.kcc.groo.user.dao.IFollowsRepository;
import com.kcc.groo.user.dao.IUsersRepository;
import com.kcc.groo.user.data.dto.FollowRequest;
import com.kcc.groo.user.data.dto.FollowResponse;
import com.kcc.groo.user.data.dto.FollowUserInfoDTO;
import com.kcc.groo.user.data.model.Follows;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor // 생성자 주입용
public class FollowService implements IFollowService {

    private final IFollowsRepository followsRepository;
    private final IUsersRepository usersRepository;
    private final INotificationService notificationService;
    private final IChallengeService challengeService; // 추가

    @Override
    public FollowResponse requestInsertFollows(String currentUserId, FollowRequest followRequest) {

        Follows newFollow = new Follows();

        if (!StringUtils.hasText(currentUserId)) { // 현재 로그인한 사용자가 없을 때
            throw new IllegalArgumentException("can not found account");
        }
        if (currentUserId.equals(followRequest.getFollowed())) { // 자기 자신 팔로우 할 경우
            throw new IllegalArgumentException("can not follow yourself");
        }
        if (usersRepository.existsByUserId(followRequest.getFollowed()) <= 0) { // 팔로우할 사용자가 존재하지 않을 경우
            throw new IllegalArgumentException("target user not found");
        }
        if (followsRepository.existsFollow(currentUserId, followRequest.getFollowed()) > 0) { // 이미 팔로우 했을 경우
            throw new IllegalArgumentException("already followed");
        }

        newFollow.setFollower(currentUserId);
        newFollow.setFollowed(followRequest.getFollowed());

        int result = followsRepository.insertFollow(newFollow);
        if (result <= 0) {
            throw new RuntimeException("failed follow");
        }

        // 팔로우 성공 → 첫 인연 뱃지 자동 검사
        try {
            challengeService.checkAndAwardBadges(currentUserId);
        } catch (Exception e) {
            log.error(" Failed to check badge after follow - userId: {}", currentUserId, e);
        }

        // 팔로우 성공 후, 상대방에게 팔로우 알림 발송
        if (result > 0) {
            try {
                notificationService.sendNotification(
                        new NotificationRequest(
                                "follow",          // type
                                "user",            // senderType
                                newFollow.getFollowId(),   // senderId
                                null,              // content (자동 생성 예정)
                                0,                 // detailSenderId
                                followRequest.getFollowed(), // 수신자 (팔로잉된 사람)
                                currentUserId      // 발신자 (팔로우한 사람)
                        )
                );
                log.info(" follow notification success: {} → {}", currentUserId, followRequest.getFollowed());
            } catch (IOException e) {
                log.error("follow notification fail - sender: {}, receiver: {}", currentUserId, followRequest.getFollowed(), e);
            }
        }

        // 팔로우 정보 반환
        Follows followInfo = followsRepository.selectFollowInfo(currentUserId, followRequest.getFollowed());
        boolean isMutual = followsRepository.isMutualFollow(currentUserId, followRequest.getFollowed()) > 0;

        return new FollowResponse(
                followInfo.getFollowId(),
                followInfo.getFollower(),
                followInfo.getFollowed(),
                isMutual,
                followInfo.getCreatedAt()
        );
    }

    @Override
    public FollowResponse getFollowInfo(String currentUserId, String targetUserId) {
        // 자기 자신을 조회하는 경우 예외 처리
        if (currentUserId.equals(targetUserId)) {
            return null;
        }

        Follows follow = followsRepository.selectFollowInfo(currentUserId, targetUserId);

        // follow가 null인 경우 (팔로우 관계가 없는 경우)
        if (follow == null) {
            return null;
        }

        boolean isMutual = followsRepository.isMutualFollow(currentUserId, targetUserId) > 0;
        return new FollowResponse(
                follow.getFollowId(),
                follow.getFollower(),
                follow.getFollowed(),
                isMutual,
                follow.getCreatedAt()
        );
    }

    @Override
    public boolean removeFollow(String currentUserId, String targetUserId) {
        if (!StringUtils.hasText(currentUserId)) {
            throw new IllegalArgumentException("cannot found logged in user");
        }

        int deleted = followsRepository.deleteFollow(currentUserId, targetUserId);
        return deleted > 0;
    }

    @Override
    public List<FollowUserInfoDTO> getFollowingList(String userId) {
        return followsRepository.selectFollowingList(userId);
    }

    @Override
    public List<FollowUserInfoDTO> getFollowerList(String userId) {
        return followsRepository.selectFollowerList(userId);
    }

    @Override
    public int getCountFollower(String userId) {
        return followsRepository.countFollower(userId);
    }

    @Override
    public int getCountFollowing(String userId) {
        return followsRepository.countFollowing(userId);
    }
}
