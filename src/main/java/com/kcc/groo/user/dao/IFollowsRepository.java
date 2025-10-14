package com.kcc.groo.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcc.groo.user.data.dto.FollowUserInfoDTO;
import com.kcc.groo.user.data.model.Follows;

@Repository
@Mapper
public interface IFollowsRepository {
	
	/**
	 * @param follows
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로우 관계 insert
	 */
	int insertFollow (Follows follows);
	
	/**
	 * @param followId
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로우 관계 단일 조회
	 */
	Follows selectFollowInfoByFollowId (int followId);
	
	/**
	 * @param follower
	 * @param followed
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로우 관계 확인
	 */
	int existsFollow (@Param("follower") String follower,
            @Param("followed") String followed);
	
	/**
	 * @param userId1
	 * @param userId2
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 맞팔로우 여부 확인 
	 */
	int isMutualFollow (@Param("userId1") String userId1,@Param("userId2") String userId2);
	
	/**
	 * @param follower
	 * @param followed
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로우한 관계 정보
	 */
	Follows selectFollowInfo(@Param("follower") String follower,
            @Param("followed") String followed);
	
	/**
	 * @param follower
	 * @param followed
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로우 관계 delete
	 */
	int deleteFollow(@Param("follower") String follower,
            @Param("followed") String followed);
	
	 /**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로잉 리스트 조회
	 */
	List<FollowUserInfoDTO> selectFollowingList(@Param("userId") String userId);
	 
	 /**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로워 리스트 조회
	 */
	List<FollowUserInfoDTO> selectFollowerList(@Param("userId") String userId);
	
	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-14
	 * 팔로워 수 카운트
	 */
	int countFollower (@Param("userId") String userId);
	
	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-14
	 * 팔로잉 수 카운트
	 */
	int countFollowing (@Param("userId") String userId);
}
