package com.kcc.groo.user.service;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.kcc.groo.user.data.dto.SignupRequest;
import com.kcc.groo.user.data.model.Users;

public interface IUserService {

	
	Users loginUser (String userId, String password);
	
	Users requestInsertUser (SignupRequest signupRequest);

	boolean confirmEmail (@Param("email") String email, @Param("code") String code);
	
	List<Users> selectAllUserId();
}
