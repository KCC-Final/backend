package com.kcc.groo.user.data.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RedisHash(value="email_verification", timeToLive = 300)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {
	
	@Id
	private String email; //pk
	private String code; //verificatin token
	private LocalDateTime expires_at; //만료 시간 (5분)
	private boolean isUsed; //토큰 사용 여부
	private LocalDateTime createdAt; //토큰 생성일

}
