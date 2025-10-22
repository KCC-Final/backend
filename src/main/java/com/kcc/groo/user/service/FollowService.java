package com.kcc.groo.user.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kcc.groo.notification.data.dto.NotificationRequest;
import com.kcc.groo.notification.service.INotificationService;
import com.kcc.groo.user.dao.IFollowsRepository;
import com.kcc.groo.user.dao.IUsersRepository;
import com.kcc.groo.user.data.dto.FollowRequest;
import com.kcc.groo.user.data.dto.FollowResponse;
import com.kcc.groo.user.data.dto.FollowUserInfoDTO;
import com.kcc.groo.user.data.model.Follows;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FollowService implements IFollowService {

	@Autowired
	IFollowsRepository followsRepository;
	@Autowired
	IUsersRepository usersRepository;
	@Autowired
	INotificationService notificationService;

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
		if (followsRepository.existsFollow(currentUserId, followRequest.getFollowed()) > 0) { //이미 팔로우 했을 경우
			throw new IllegalArgumentException("already followed");
		}
		newFollow.setFollower(currentUserId);
		newFollow.setFollowed(followRequest.getFollowed());

		int result = followsRepository.insertFollow(newFollow);

		if (result <= 0) {
			throw new RuntimeException("failed follow");
		}
		
		// 팔로우 성공 후
		 if (result > 0 && !followRequest.getFollowed().equals(currentUserId)) {
			 try {
				notificationService.sendNotification(
						    new NotificationRequest(
						        "follow",          // type
						        "user",            // senderType
						        newFollow.getFollowId(),                 // senderId
						        null,              // content (자동 생성 예정)
						        0,                 // detailSenderId
						        followRequest.getFollowed(), // userId
						        currentUserId      // senderUserId
						    )
						);
			 } catch (IOException e) {
				e.printStackTrace();
			 }

		    }

		Follows followInfo = followsRepository.selectFollowInfo(currentUserId, followRequest.getFollowed());
		boolean isMutual = followsRepository.isMutualFollow(currentUserId, followRequest.getFollowed()) > 0;

		return new FollowResponse(followInfo.getFollowId(), followInfo.getFollower(), followInfo.getFollowed(),
				isMutual, followInfo.getCreatedAt());
	}
	


	@Override
	public FollowResponse getFollowInfo(String currentUserId, String targetUserId) {
		Follows follow = followsRepository.selectFollowInfo(currentUserId, targetUserId);
		boolean isMutual = followsRepository.isMutualFollow(currentUserId, targetUserId) > 0;
		return new FollowResponse(follow.getFollowId(), follow.getFollower(), follow.getFollowed(), isMutual,
				follow.getCreatedAt());
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
		// TODO Auto-generated method stub
		return followsRepository.countFollower(userId);
	}

	@Override
	public int getCountFollowing(String userId) {
		// TODO Auto-generated method stub
		return followsRepository.countFollowing(userId);
	}

}