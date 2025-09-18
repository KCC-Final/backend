package com.kcc.groo.user.service;

import java.time.LocalDate;

import com.kcc.groo.user.data.model.Users;

public interface IUserService {

	
	Users loginUser (String userId, String password);
	
	Users insertUser (String userId, String rawPassword, String email, String nickName, char gender, String name, 
			LocalDate birth); //2025-09-18 (이메일 인증 미포함 회원 가입) kys
	
}
