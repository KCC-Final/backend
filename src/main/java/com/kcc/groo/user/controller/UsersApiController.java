package com.kcc.groo.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
//AuthenticationManager, UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//Authentication
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.config.CustomUserDetails;
import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.user.dao.IUsersRepository;
import com.kcc.groo.user.data.dto.FindUserIdRequest;
import com.kcc.groo.user.data.dto.LoginRequest;
import com.kcc.groo.user.data.dto.ResetPasswordRequest;
import com.kcc.groo.user.data.dto.SignupRequest;
import com.kcc.groo.user.data.model.Users;
import com.kcc.groo.user.service.EmailVerificationService;
import com.kcc.groo.user.service.MailService;
import com.kcc.groo.user.service.TokenService;
import com.kcc.groo.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
//Http 응답 관련
import jakarta.servlet.http.HttpServletResponse;
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
	@Autowired
    AuthenticationManager authenticationManager;
	@Autowired
	TokenService tokenService;
	@Autowired
	IUsersRepository usersRepository;

	/**
	 * @param loginRequest
	 * @param request
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-23 사용자 로그인 요청 처리
	 * 
	 * @modified 2025-09-29
	 * 쿠키 / redis에 토큰 저장하는 코드 추가
	 */
	@PostMapping("auth/login")
	public ResponseEntity<CommonResponse<?>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		
		long accessTokenValidTime = 15 * 60 * 1000L; //15min
		long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L; //7 days
		
	    Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	            		loginRequest.getUserId(), loginRequest.getPassword())
	    );

	    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
	    Users user = userDetails.getUser();

	    String accessToken = jwtTokenProvider.generateAccessToken(user);
	    String refreshToken = jwtTokenProvider.generateRefreshToken(user);
	    
	    tokenService.saveToken(loginRequest.getUserId(), refreshToken, refreshTokenValidTime);

	    ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
	            .httpOnly(true)              
	            .secure(false)               
	            .sameSite("Lax")             
	            .path("/")                   
	            .maxAge(refreshTokenValidTime)    
	            .build();
	    
	    ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
	            .httpOnly(true)             
	            .secure(false)   //운영 환경에서는 true    
	            .sameSite("Lax")            
	            .path("/")                  
	            .maxAge(accessTokenValidTime)   
	            .build();
	    response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
	    response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
	    
	   //tokens
	    Map <String, String> tokens = new HashMap<>();
	    tokens.put("accessToken", accessToken);
	    tokens.put("refreshToken", refreshToken);

	    return ResponseEntity.ok(new CommonResponse<>("tokens", tokens));
	}
	
	/**
	 * @param refreshToken
	 * @return
	 * @created 2025-09-29
	 * @author kys
	 * 토큰 재발급 (header 또는 cookie에서 refresh token 가져와 access token 새로 발급
	 */
	@PostMapping("token-refresh")
	public ResponseEntity<CommonResponse<?>> refresh(@CookieValue("refreshToken") String cookieRefreshToken,
			@RequestHeader(value = "Authorization", required = false) String headerRefreshToken) {
	    
		String refreshToken = null;
		if(headerRefreshToken != null) {
			refreshToken = headerRefreshToken;
		} else {
			refreshToken = cookieRefreshToken;
		}
		
		if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonResponse<>("Invalid or expired refresh token", null));
	    }

	    String userId = jwtTokenProvider.getUserId(refreshToken);
	    String storedToken = tokenService.getToken(userId);
	    
	    if (!refreshToken.equals(storedToken)) {
	    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CommonResponse<>("Refresh token does not match", null));
	    }
	    
	    Users user = usersRepository.selectUserByUserId(userId);
	    if (user == null) {
	    	return  ResponseEntity.badRequest().body(new CommonResponse<>("User not found", null));
	    } else {
	    	String newAccessToken = jwtTokenProvider.generateAccessToken(user);
	    	return ResponseEntity.ok(new CommonResponse<>("accessToken", newAccessToken));
	    }
	}
	
	/**
	 * @param response
	 * @return
	 * @created 2025-09-29
	 * @author kys
	 * 로그아웃 시 redis에 저장되어 있던 refresh token 삭제
	 * 로그아웃 시 cookie에 저장되어 있던 access token 삭제
	 * 인증된 사용자만 로그아웃 가능 (추후 수정 예정)
	 */
//	@PreAuthorize("isAuthenticated()")
	@PostMapping("auth/logout")
	public ResponseEntity<CommonResponse<?>> logout(HttpServletResponse response,
			@CookieValue(value="refreshToken", required = false) String refreshToken,
			@CookieValue(value="accessToken", required = false) String accessToken) {
		
	    ResponseCookie deleteRefreshCookie = ResponseCookie.from("refreshToken", "") //쿠키에서 refresh token 즉시 삭제
	            .httpOnly(true)
	            .secure(true) //운영 환경에서는 true
	            .path("/")
	            .sameSite("Strict")
	            .maxAge(0)   // 즉시 만료
	            .build();
	    response.addHeader(HttpHeaders.SET_COOKIE, deleteRefreshCookie.toString());
	    
	    ResponseCookie deleteAccessCookie = ResponseCookie.from("accessToken", "")
	    		.httpOnly(true)
	            .secure(true) //운영 환경에서는 true
	            .path("/")
	            .sameSite("Strict")
	            .maxAge(0)   // 즉시 만료
	            .build();
	    response.addHeader(HttpHeaders.SET_COOKIE, deleteAccessCookie.toString());
	    
	    if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) { //redis에서 즉시 삭제
	    	String userId = jwtTokenProvider.getUserId(refreshToken);
	    	tokenService.deleteToken(userId);
	    }

	    return ResponseEntity.ok(new CommonResponse<>("Logged out successfully", null));
	    
	}




	/**
	 * @param email
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-23
	 * 회원가입 시 입력된 이메일에 인증 코드를 전송
	 */
	@PostMapping("/email")
	public ResponseEntity<CommonResponse<?>> confirmEmail (@RequestParam("purpose") String purpose, @RequestParam("email") String email) {
		String code = emailVerificationService.createVerificationCode(purpose, email);
		mailService.sendVerificationEmail(email, code);
		
		if (purpose.equals("signup")) {
			return ResponseEntity.ok(new CommonResponse<>("Signup verification code sent", code));
		} else if (purpose.equals("findId")) {
			return ResponseEntity.ok(new CommonResponse<>("FindId verification code sent", code));
		} else {
			return  ResponseEntity.badRequest().body(new CommonResponse<>("Email sending failed", null));
		}
	}
	
	/**
	 * @param email
	 * @param code
	 * @param request
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-23
	 * 회원가입 시 전송된 인증 코드의 일치 여부를 확인
	 * 
	 * 
	 */
	@PostMapping("/email/verify")
	public ResponseEntity<CommonResponse<?>> verifyEmailCode (@RequestParam("purpose") String purpose, @RequestParam("email") String email, @RequestParam("code") String code, HttpServletRequest request) {
		boolean verified = emailVerificationService.verifyCode(purpose, email, code);
		if (verified) {
			request.getSession().setAttribute("verifiedEmail", email);
			return ResponseEntity
                    .ok(new CommonResponse<>("Email verification success", email));
			} else {
				return ResponseEntity
	                    .badRequest()
	                    .body(new CommonResponse<>("Email verification failed", null));		}
	}
	
	/**
	 * @param request
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-23 이메일 인증 및 약관 확인 여부를 체크하고 회원 데이터 저장
	 * 
	 * @modified 2025-09-25
	 */
	@PostMapping("users/signup")
	public ResponseEntity<CommonResponse<?>> signup(@Valid @RequestBody SignupRequest signupRequest, HttpServletRequest request) {
		
		if (!emailVerificationService.isVerified("signup", signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("Email verification required", null));
		}

		if (!signupRequest.isCheckPrivacy() || !signupRequest.isCheckService()) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("Terms agreement required", null));
		}

		if (!signupRequest.getPassword1().equals(signupRequest.getPassword2())) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("Passwords do not match", null));
		}
		
		if (userService.existsByUserId(signupRequest.getUserId()) > 0) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("This ID is already in use. Please choose another one", null));
		}

			Users newUser = userService.requestInsertUser(signupRequest);

			if (newUser != null) {
				emailVerificationService.clearVerified("signup", signupRequest.getEmail());
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>("Signup success", newUser));
	}
	

	/**
	 * @param userId
	 * @param request
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-24
	 * 회원가입 시 아이디 중복체크
	 */
	@PostMapping("/users/id/verify")
	public ResponseEntity<CommonResponse<?>> checkId (@RequestParam("userId") String userId, HttpServletRequest request) {
		if (userService.existsByUserId(userId) > 0) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("This ID is already in use. Please choose another one", userId));
		} else {
			return ResponseEntity
                    .ok(new CommonResponse<>("check userId success", userId));
		}
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
	        request.getSession().setAttribute("findUserId", userId); //userId session에 저장 (pw 재설정을 위해)
	        return ResponseEntity.ok(new CommonResponse<>("Find userId success", userId));
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest()
	                .body(new CommonResponse<>(e.getMessage(), null));
	    }
	}
	
	/**
	 * @param resetPasswordRequest
	 * @param request
	 * @return
	 * @created 2025-09-25
	 * 아이디 찾기 후 사용자 비밀번호 재설정 가능
	 */
	@PostMapping("users/password")
	public ResponseEntity<CommonResponse<?>> resetPassword (@Valid @RequestBody ResetPasswordRequest resetPasswordRequest, HttpServletRequest request) {
		//updatePassword
		String userId = (String)request.getSession().getAttribute("findUserId");
		
		if (!resetPasswordRequest.getPassword1().equals(resetPasswordRequest.getPassword2())) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("Passwords do not match", null));
		}
		
		Users resetPw = userService.resetPassword(userId, resetPasswordRequest.getPassword1());
		return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse<>("updated password info success", resetPw));
		
	}
	


}
