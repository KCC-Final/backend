package com.kcc.groo.user.service;

import org.springframework.data.repository.query.Param;

public interface IEmailVerificationService {
	
	/**
	 * @param purpose
	 * @param email
	 * @return
	 * @author kys
	 * @created 2025-09-23
	 * 이메일 인증용 코드 생성
	 */
	String createVerificationCode (@Param("purpose") String purpose, @Param("email") String email);
	
	/**
	 * @param purpose
	 * @param email
	 * @param inputCode
	 * @return
	 * @author kys
	 * @created 2025-09-23
	 * 이메일 인증용 코드와 입력된 코드 확인
	 */
	boolean verifyCode(@Param("purpose") String purpose, @Param("email") String email, @Param("inputCode") String inputCode);
	
	/**
	 * @param purpose
	 * @param email
	 * @return
	 * @author kys
	 * @created 2025-09-23
	 * 이메일 인증 완료 여부 확인
	 */
	boolean isVerified(String purpose, String email);

}
