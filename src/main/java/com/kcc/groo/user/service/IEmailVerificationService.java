package com.kcc.groo.user.service;

import org.springframework.data.repository.query.Param;

public interface IEmailVerificationService {
	
	String createVerificationCode (@Param("email") String email);
	boolean verifyCode(@Param("email") String email, @Param("inputCode") String inputCode);
	boolean isVerified(String email);


}
