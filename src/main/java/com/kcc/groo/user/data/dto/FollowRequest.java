package com.kcc.groo.user.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kys
 * @created 2025-10-07
 * follow 관계 생성을 위한 dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowRequest {
	
	private String follower;
	private String followed;

}
