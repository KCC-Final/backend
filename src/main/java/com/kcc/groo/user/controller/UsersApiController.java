package com.kcc.groo.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.user.data.dto.LoginRequest;
import com.kcc.groo.user.data.model.Users;
import com.kcc.groo.user.service.UserService;

//import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.RequestBody;
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
	public String login (@Valid @RequestBody LoginRequest loginReq, HttpServletRequest request) {
		log.info("Login request: {}", loginReq);
		Users users = userService.loginUser(loginReq.getUserId(), loginReq.getPassword());

		if (users == null) {
			throw new IllegalArgumentException("can not found account | user info is null");
		}
		
		if (!passwordEncoder.matches(loginReq.getPassword(), users.getPassword())) {
			throw new IllegalArgumentException("can not found account | unmatch user password");
		}
		return jwtTokenProvider.generateToken(users);
	}


}
