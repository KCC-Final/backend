package com.kcc.groo.user.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenService {
	
	@Autowired
	StringRedisTemplate redisTemplate;
	
	private static final String KEY_PREFIX = "refreshToken:";
	
	/**
	 * @param userId
	 * @param refreshToken
	 * @param expiration
	 * @created 2025-09-29
	 * @author kys
	 * 
	 * refresh 토큰 redis에 저장
	 */
	public void saveToken (String userId, String refreshToken, long expiration) {
		ValueOperations <String, String> ops = redisTemplate.opsForValue();
		ops.set(KEY_PREFIX+userId, refreshToken, Duration.ofMillis(expiration));
		log.info("refreshToken: " + refreshToken);
		log.info("Saving token with key: " + KEY_PREFIX + userId);
	}
	
	/**
	 * @param userId
	 * @return
	 * @created 2025-09-29
	 * @author kys
	 * redis에 저장된 토큰 select
	 */
	public String getToken (String userId) {
		return redisTemplate.opsForValue().get(KEY_PREFIX+userId);
	}
	
	/**
	 * @param userId
	 * @created 2025-09-29
	 * @author kys
	 * redis에 저장된 토큰 삭제 (로그아웃 시 사용)
	 */
	public void deleteToken (String userId) {
		redisTemplate.delete(KEY_PREFIX+userId);
	}
	
	/**
	 * @param userId
	 * @param token
	 * @return
	 * @created 2025-09-29
	 * @author kys
	 * 토큰 검증
	 */
	public boolean isTokenValid (String userId, String token) {
		String storedToken = getToken(userId);
		
		if (storedToken.isEmpty() || storedToken == null || !storedToken.equals(token)) {
			return false;
		} else {
			return true;
		}
	}

}
