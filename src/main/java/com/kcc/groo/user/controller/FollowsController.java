package com.kcc.groo.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.user.data.dto.FollowRequest;
import com.kcc.groo.user.data.dto.FollowResponse;
import com.kcc.groo.user.data.dto.FollowUserInfoDTO;
import com.kcc.groo.user.service.FollowService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class FollowsController {

	@Autowired
	FollowService followService;
	@Autowired
	JwtTokenProvider jwtTokenProvider;

	/**
	 * @param followRequest
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로우 관계 생성
	 */
	@PostMapping("/follows")
	public ResponseEntity<CommonResponse<?>> createFollow(@RequestBody FollowRequest followRequest,
			HttpServletRequest request) {
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);

		FollowResponse newFollows = followService.requestInsertFollows(userId, followRequest);

		return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>("Follow success", newFollows));

	}

	/**
	 * @param request
	 * @param targetUserId
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 로그인한 사용자가 팔로우한 사람 확인
	 */
	@GetMapping("/follows/{targetId}")
	public ResponseEntity<CommonResponse<?>> getFollowInfo(HttpServletRequest request,
			@PathVariable("targetId") String targetId) {
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
		FollowResponse followResponse = followService.getFollowInfo(userId, targetId);
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new CommonResponse<>("Follow information", followResponse));
	}
	
	/**
	 * @param request
	 * @param targetId
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로우 취소
	 */
	@DeleteMapping("/follows/{targetId}")
	public ResponseEntity<CommonResponse<?>> deleteFollow (HttpServletRequest request,
			@PathVariable("targetId") String targetId) {
		
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
		boolean success = followService.removeFollow(userId, targetId);
		
        if (!success) {
        	return ResponseEntity.badRequest()
					.body(new CommonResponse<>("delete failed", null));
        }
        
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new CommonResponse<>("delete success", null));
	}
	
	/**
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로잉 리스트
	 */
	@GetMapping("/following")
    public ResponseEntity<CommonResponse<?>> getFollowingList(HttpServletRequest request) {
		
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        List<FollowUserInfoDTO> followingList = followService.getFollowingList(userId);
        
        return ResponseEntity.ok(new CommonResponse<>("Following list", followingList));
    }

    /**
     * @param request
     * @return
	 * @author kys
	 * @created 2025-10-07
	 * 팔로워 리스트
     */
    @GetMapping("/followers")
    public ResponseEntity<CommonResponse<?>> getFollowerList(HttpServletRequest request) {
    	
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        List<FollowUserInfoDTO> followerList = followService.getFollowerList(userId);
        
        return ResponseEntity.ok(new CommonResponse<>("Follower list", followerList));
    }
    
    /**
     * @param request
     * @return
	 * @author kys
	 * @created 2025-10-14
	 * 팔로워 수 카운트
     */
    @GetMapping("/followers-count")
    public ResponseEntity<CommonResponse<?>> getFollowerCount (HttpServletRequest request) {
    	
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

       int countFollowers = followService.getCountFollower(userId);
        
        return ResponseEntity.ok(new CommonResponse<>("get Follower Count", countFollowers));
    }
    
    /**
     * @param request
     * @return
	 * @author kys
	 * @created 2025-10-14
	 * 팔로잉 수 카운트
     */
    @GetMapping("/following-count")
    public ResponseEntity<CommonResponse<?>> getfollowingCount (HttpServletRequest request) {
    	
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        int countfollowing = followService.getCountFollower(userId);
        
        return ResponseEntity.ok(new CommonResponse<>("get following Count", countfollowing));
    }
}