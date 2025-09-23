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
	public String createVerificationCode(String email) {
		String code = String.valueOf((int) (Math.random() * 900000) + 100000);
		redisTemplate.opsForValue().set("verify:" + email, code, 5, TimeUnit.MINUTES);

		return code;
	}

	@Override
	public boolean verifyCode(String email, String inputCode) {
		String savedCode = (String) redisTemplate.opsForValue().get("verify:" + email);
		if (savedCode == null || !savedCode.equals(inputCode)) {
			return false;
		}

		redisTemplate.opsForValue().set("verified:" + email, "true", 10, TimeUnit.MINUTES);

		// 일치하면 즉시 삭제 (재사용 방지)
		redisTemplate.delete("verify:" + email);
		return true;
	}

	public boolean isVerified(String email) {
		return "true".equals(redisTemplate.opsForValue().get("verified:" + email));
	}

	public void clearVerified(String email) {
		redisTemplate.delete("verified:" + email);
	}
}
