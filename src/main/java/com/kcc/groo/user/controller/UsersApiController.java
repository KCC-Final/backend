package com.kcc.groo.user.controller;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.user.data.dto.LoginRequest;
import com.kcc.groo.user.data.dto.SignupRequest;
import com.kcc.groo.user.data.model.Users;
import com.kcc.groo.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UsersApiController {
	
	@Autowired
	UserService userService;
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@PostMapping("/auth/login")
	public CommonResponse<String> login (@Valid @RequestBody LoginRequest loginReq, HttpServletRequest request) {
		log.info("Login request: {}", loginReq);
		Users users = userService.loginUser(loginReq.getUserId(), loginReq.getPassword());

		if (users == null) {
			throw new IllegalArgumentException("can not found account | user info is null");
		}
		
		if (!passwordEncoder.matches(loginReq.getPassword(), users.getPassword())) {
			throw new IllegalArgumentException("can not found account | unmatch user password");
		}
		return new CommonResponse<String>("Login Success", jwtTokenProvider.generateToken(users));
	}
	
	@PostMapping("/users/signup")
	public CommonResponse<Users> signup (@Valid @RequestBody SignupRequest signupReq, HttpServletRequest request) {
		log.info("signup request: {}", signupReq);
		
		String signupId = signupReq.getUserId();
		List<Users> userIds = userService.selectAllUserId();
		
		//id 중복 체크
		for (int i = 0; i < userIds.size(); i++) {
			if (signupId.equals(userIds.get(i))) {
				throw new IllegalArgumentException("This ID already exists. Please enter a new ID.");
			}
		}
		
		//비밀번호 확인
		if (!signupReq.getPassword1().equals(signupReq.getPassword2())) {
			throw new IllegalArgumentException("The password does not match. Please re-enter it.");
		}
		try {
			Users newUser = userService.insertUser(signupReq.getUserId(), signupReq.getPassword1(), signupReq.getEmail(), 
					signupReq.getNickname(), signupReq.getGender(), signupReq.getName(), signupReq.getBirth());
			
			return new CommonResponse<Users>("Insert User Success", newUser);
		} catch (Exception e) {
			e.printStackTrace();
			return new CommonResponse<>(e.getMessage(), null);
		}
	}
}
