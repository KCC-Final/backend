package com.kcc.groo.user.service;

import java.time.LocalDate;
import java.util.List;

import com.kcc.groo.user.data.dto.SignupRequest;
import com.kcc.groo.user.data.model.Users;

public interface IUserService {

	
	Users loginUser (String userId, String password);
	
	Users insertUser (SignupRequest signupRequest); //2025-09-18 (이메일 인증 미포함 회원 가입) kys
	
	String verificationEmail (String email);
	
	List<Users> selectAllUserId();
}
