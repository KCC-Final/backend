package com.kcc.groo.user.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kcc.groo.user.data.dto.FollowRequest;
import com.kcc.groo.user.data.dto.FollowResponse;
import com.kcc.groo.user.data.dto.FollowUserInfoDTO;

public interface IFollowService {
	
	/**
	 * @param currentUserId
	 * @param followRequest
	 * @return
	 * @author kys
	 * @created 2025-10-07 
	 * 팔로우 관계 생성
	 */
	FollowResponse requestInsertFollows (String currentUserId, FollowRequest followRequest);
	
	/**
	 * @param currentUserId
	 * @param targetUserId
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로우 정보
	 */
	FollowResponse getFollowInfo(String currentUserId, String targetUserId);
	
	/**
	 * @param currentUserId
	 * @param targetUserId
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로우 관계 삭제
	 */
	boolean removeFollow(String currentUserId, String targetUserId);
	
	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로잉 리스트
	 */
	List<FollowUserInfoDTO> getFollowingList(String userId);
	
    /**
     * @param userId
     * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로워 리스트
     */
    List<FollowUserInfoDTO> getFollowerList(String userId);
	
    /**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-14
	 * 팔로워 수 카운트
	 */
    int getCountFollower (String userId);
    
	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-14
	 * 팔로잉 수 카운트
	 */
	int getCountFollowing (String userId);

}
