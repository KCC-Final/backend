package com.kcc.groo.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.user.data.dto.FindUserIdRequest;
import com.kcc.groo.user.data.dto.LoginRequest;
import com.kcc.groo.user.data.dto.SignupRequest;
import com.kcc.groo.user.data.model.Users;
import com.kcc.groo.user.service.EmailVerificationService;
import com.kcc.groo.user.service.MailService;
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
	@Autowired
	EmailVerificationService emailVerificationService;
	@Autowired
	MailService mailService;

	/**
	 * @param loginRequest
	 * @param request
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-23 사용자 로그인 요청 처리
	 */
	@PostMapping("/auth/login")
	public ResponseEntity<CommonResponse<?>> login(@Valid @RequestBody LoginRequest loginRequest,
			HttpServletRequest request) {
		log.info("Login request: {}", loginRequest);
		Users users = userService.loginUser(loginRequest.getUserId(), loginRequest.getPassword());

		if (users == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CommonResponse<>("Account not found", null));
		}

		if (!passwordEncoder.matches(loginRequest.getPassword(), users.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new CommonResponse<>("Invalid credentials", null));
		}
		String token = jwtTokenProvider.generateToken(users);
		return ResponseEntity.ok(new CommonResponse<>("Login success", token));
	}

	/**
	 * @param email
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-23
	 * 회원가입 시 입력된 이메일에 인증 코드를 전송
	 * 
	 * @modified 2025-09-24 아이디 찾기 시 이메일 인증과의 분리를 위해 api 추가 아이디 찾기 시 이메일 인증을 위한 메서드명 구분
	 */
	@PostMapping("/email/signup")
	public ResponseEntity<CommonResponse<?>> sendSignupCode(@RequestParam("email") String email) {
		String code = emailVerificationService.createVerificationCode("signup", email);
		mailService.sendVerificationEmail(email, code);

		return ResponseEntity.ok(new CommonResponse<>("Signup verification code sent", null));

	}

	/**
	 * @param email
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-24
	 * 아이디 찾기를 위해 입력된 이메일에 인증 코드를 전송
	 * 
	 */
	@PostMapping("/email/find-id")
	public ResponseEntity<CommonResponse<?>> sendFindIdCodeAndCheckName(@RequestParam("email") String email) {
		String code = emailVerificationService.createVerificationCode("findId", email);
		mailService.sendVerificationEmail(email, code);

		return ResponseEntity.ok(new CommonResponse<>("FindId verification code sent", null));

	}

	/**
	 * @param email
	 * @param code
	 * @param request
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-23 전송된 인증 코드의 일치 여부를 확인
	 * 
	 * @modified 2025-09-24 아이디 찾기 / 회원가입 / 회원 정보 수정 시 공통적으로 사용할 수 있도록 수정 (purpose 를 통해 구분)
	 */
	@PostMapping("/email/verify")
	public ResponseEntity<CommonResponse<?>> verifyEmailCode(@RequestParam("purpose") String purpose,
			@RequestParam("email") String email, @RequestParam("code") String code, HttpServletRequest request) {
		boolean verified = emailVerificationService.verifyCode(purpose, email, code);
		if (verified) {
			request.getSession().setAttribute("verifiedEmail", email);
			return ResponseEntity.ok(new CommonResponse<>("Email verification success", null));
		} else {
			return ResponseEntity.badRequest().body(new CommonResponse<>("Email verification failed", null));
		}
	}

	/**
	 * @param request
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-23 이메일 인증 및 약관 확인 여부를 체크하고 회원 데이터 저장
	 * 
	 * @modified 2025-09-24
	 */
	@PostMapping("users/signup")
	public ResponseEntity<CommonResponse<?>> signup(@Valid @RequestBody SignupRequest request) {
		if (!emailVerificationService.isVerified("signup", request.getEmail())) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("Email verification required", null));
		}

		if (!request.isCheckPrivacy() || !request.isCheckService()) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("Terms agreement required", null));
		}

		if (!request.getPassword1().equals(request.getPassword2())) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("Passwords do not match", null));
		}

		Users newUser = userService.requestInsertUser(request);

		emailVerificationService.clearVerified("signup", request.getEmail());

		return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>("Signup success", newUser));
	}


	/**
	 * @param findUserIdRequest
	 * @param request
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-24
	 * 사용자 아이디를 찾기 위해 사용자명과 이메일 입력 -> 검증 후 사용자 아이디 반환
	 */
	@PostMapping("users/id")
	public ResponseEntity<CommonResponse<?>> findUserId(
	        @Valid @RequestBody FindUserIdRequest findUserIdRequest,
	        HttpServletRequest request) {

	    String name = findUserIdRequest.getName();
	    String email = findUserIdRequest.getEmail();

	    String verifiedEmail = (String) request.getSession().getAttribute("verifiedEmail");
	    if (verifiedEmail == null || !verifiedEmail.equals(email)) {
	        return ResponseEntity.badRequest()
	                .body(new CommonResponse<>("Email not verified", null));
	    }

	    if (userService.existsByUserName(name) <= 0) {
	        return ResponseEntity.badRequest()
	                .body(new CommonResponse<>("User name not found", null));
	    }

	    try {
	        String userId = userService.findUserIdByNameAndEmail(name, email);
	        return ResponseEntity.ok(new CommonResponse<>("Find userId success", userId));
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest()
	                .body(new CommonResponse<>(e.getMessage(), null));
	    }
	}


}
