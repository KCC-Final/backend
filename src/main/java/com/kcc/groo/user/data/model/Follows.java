package com.kcc.groo.user.data.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follows {
	
	private int followId;
	private LocalDateTime createdAt;
	private String follower;
	private String followed;

}
