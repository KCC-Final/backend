package com.kcc.groo.user.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.kcc.groo.user.dao.EmailVerificationRepository;

@Service
public class EmailVerificationService implements IEmailVerificationService {

	@Autowired
	EmailVerificationRepository emailVerificationRepository;
	@Autowired
	RedisTemplate<String, String> redisTemplate;

	@Override
	public String createVerificationCode(String purpose, String email) {
		String code = String.valueOf((int) (Math.random() * 900000) + 100000);
		redisTemplate.opsForValue().set(purpose + ":verify:" + email, code, 5, TimeUnit.MINUTES);
		return code;
	}

	@Override
	public boolean verifyCode(String purpose, String email, String inputCode) {
		String savedCode = redisTemplate.opsForValue().get(purpose + ":verify:" + email);
		if (savedCode == null || !savedCode.equals(inputCode)) {
			return false;
		}

		redisTemplate.delete(purpose + ":verify:" + email);
		redisTemplate.opsForValue().set(purpose + ":verified:" + email, "true", 10, TimeUnit.MINUTES);
		return true;
	}

	public boolean isVerified(String purpose, String email) {
		return "true".equals(redisTemplate.opsForValue().get(purpose + ":verified:" + email));
	}

	public void clearVerified(String purpose, String email) {
		redisTemplate.delete(purpose + ":verified:" + email);	}
	
	
}
