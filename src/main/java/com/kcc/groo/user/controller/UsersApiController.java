package com.kcc.groo.user.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.config.CustomUserDetails;
import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.user.dao.IUsersRepository;
import com.kcc.groo.user.data.dto.FindUserIdRequest;
import com.kcc.groo.user.data.dto.LoginRequest;
import com.kcc.groo.user.data.dto.ResetPasswordRequest;
import com.kcc.groo.user.data.dto.SignupRequest;
import com.kcc.groo.user.data.dto.UserUpdateRequest;
import com.kcc.groo.user.data.model.Users;
import com.kcc.groo.user.service.EmailVerificationService;
import com.kcc.groo.user.service.MailService;
import com.kcc.groo.user.service.TokenService;
import com.kcc.groo.user.service.UserService;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
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

	private static final long ACCESS_TOKEN_VALID_TIME = 15 * 60 * 1000L; // 15min
	private static final long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L; // 7 days
	private static final long ACCESS_TOKEN_VALID_TIME_SEC = ACCESS_TOKEN_VALID_TIME / 1000;
	private static final long REFRESH_TOKEN_VALID_TIME_SEC = REFRESH_TOKEN_VALID_TIME / 1000;

	/**
	 * @author uyh
	 * @param name
	 * @param value
	 * @param maxAgeSec
	 * @return
	 * @created 2025-09-29 쿠키 생성
	 * @modified 2025-10-11 크로스 도메인 쿠키 설정 추가
	 */
	private ResponseCookie buildCookie(String tokenName, String value, long maxAgeSec) {
	    return ResponseCookie.from(tokenName, value)
	        .httpOnly(true)
	        .secure(true)  // ✅ true로 변경
	        .domain(".groo.site")  // ✅ 이 줄 추가
	        .path("/")
	        .sameSite("None")  // ✅ None으로 변경
	        .maxAge(maxAgeSec)
	        .build();
	}
	/**
	 * @param name
	 * @return
	 * @author kys
	 * @created 2025-09-29 쿠키 삭제
	 */
	private ResponseCookie buildDeleteCookie(String tokenName) {
		return buildCookie(tokenName, "", 0);
	}

	/**
	 * @param loginRequest
	 * @param request
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-23 사용자 로그인 요청 처리
	 * 
	 * @modified 2025-09-29 쿠키 / redis에 토큰 저장하는 코드 추가
	 * 
	 *           쿠키 만료 기간 수정
	 */
	@PostMapping("/auth/login")
	public ResponseEntity<CommonResponse<?>> login(@RequestBody LoginRequest loginRequest,
			HttpServletResponse response) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUserId(), loginRequest.getPassword()));

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		Users user = userDetails.getUser();

		String accessToken = jwtTokenProvider.generateAccessToken(user);
		String refreshToken = jwtTokenProvider.generateRefreshToken(user);
		tokenService.saveToken(loginRequest.getUserId(), refreshToken, REFRESH_TOKEN_VALID_TIME);

		response.addHeader(HttpHeaders.SET_COOKIE,
				buildCookie("accessToken", accessToken, ACCESS_TOKEN_VALID_TIME_SEC).toString());
		response.addHeader(HttpHeaders.SET_COOKIE,
				buildCookie("refreshToken", refreshToken, REFRESH_TOKEN_VALID_TIME_SEC).toString());

		Map<String, String> tokens = Map.of("accessToken", accessToken, "refreshToken", refreshToken);
		return ResponseEntity.ok(new CommonResponse<>("Login success", tokens));
	}

	/**
	 * @param refreshToken
	 * @return
	 * @created 2025-09-29
	 * @author kys 토큰 재발급 (header 또는 cookie에서 refresh token 가져와 access token 새로 발급
	 */
	@PostMapping("/token-refresh")
	public ResponseEntity<CommonResponse<?>> refresh(HttpServletRequest request, HttpServletResponse response) {

		String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

		if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new CommonResponse<>("Invalid or expired refresh token", null));
		}

		String userId = jwtTokenProvider.getUserId(refreshToken);
		String storedToken = tokenService.getToken(userId);

		if (!refreshToken.equals(storedToken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new CommonResponse<>("Refresh token does not match", null));
		}

		Users user = usersRepository.selectUserByUserId(userId);

		if (user == null) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("User not found", null));
		} else {
			String newAccessToken = jwtTokenProvider.generateAccessToken(user);
			response.addHeader(HttpHeaders.SET_COOKIE,
					buildCookie("accessToken", newAccessToken, ACCESS_TOKEN_VALID_TIME_SEC).toString());
			return ResponseEntity.ok(new CommonResponse<>("New access token issued", newAccessToken));
		}
	}

	/**
	 * @param response
	 * @return
	 * @created 2025-09-29
	 * @author kys 로그아웃 시 redis에 저장되어 있던 refresh token 삭제 로그아웃 시 cookie에 저장되어 있던
	 *         access token 삭제 인증된 사용자만 로그아웃 가능 (추후 수정 예정)
	 */
//	@PreAuthorize("isAuthenticated()")
	@PostMapping("/auth/logout")
	public ResponseEntity<CommonResponse<?>> logout(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
		response.addHeader(HttpHeaders.SET_COOKIE, buildDeleteCookie("accessToken").toString());
		response.addHeader(HttpHeaders.SET_COOKIE, buildDeleteCookie("refreshToken").toString());

		if (refreshToken != null)
			tokenService.deleteToken(jwtTokenProvider.getUserId(refreshToken));

		return ResponseEntity.ok(new CommonResponse<>("Logged out successfully", null));
	}

	/**
	 * @param email
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-23 회원가입 시 입력된 이메일에 인증 코드를 전송
	 */
	@PostMapping("/email")
	public ResponseEntity<CommonResponse<?>> confirmEmail(@RequestParam("purpose") String purpose,
			@RequestParam("email") String email) {
		String code = emailVerificationService.createVerificationCode(purpose, email);
		mailService.sendVerificationEmail(email, code);

		if (purpose.equals("signup")) {
			return ResponseEntity.ok(new CommonResponse<>("Signup verification code sent", code));
		} else if (purpose.equals("findId")) {
			return ResponseEntity.ok(new CommonResponse<>("FindId verification code sent", code));
		} else if (purpose.equals("updateEmail")) {
			return ResponseEntity.ok(new CommonResponse<>("updateEmail verification code sent", code));
		} else {
			return ResponseEntity.badRequest().body(new CommonResponse<>("Email sending failed", null));
		}
	}

	/**
	 * @param email
	 * @param code
	 * @param request
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-23 전송된 인증 코드의 일치 여부를 확인
	 * 
	 * 
	 */
	@PostMapping("/email/verify")
	public ResponseEntity<CommonResponse<?>> verifyEmailCode(@RequestParam("purpose") String purpose,
			@RequestParam("email") String email, @RequestParam("code") String code, HttpServletRequest request) {
		boolean verified = emailVerificationService.verifyCode(purpose, email, code);
		if (verified) {
			request.getSession().setAttribute("verifiedEmail", email);
			usersRepository.updateEmailVerified(email, true);
			return ResponseEntity.ok(new CommonResponse<>("Email verification success", email));
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
	 * @modified 2025-09-25
	 */
	@PostMapping("/users/signup")
	public ResponseEntity<CommonResponse<?>> signup(@Valid @RequestBody SignupRequest signupRequest,
			HttpServletRequest request) {

		if (!emailVerificationService.isVerified("signup", signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("signup Email verification required", null));
		}

		if (!signupRequest.isCheckPrivacy() || !signupRequest.isCheckService()) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("signup Terms agreement required", null));
		}

		if (!signupRequest.getPassword1().equals(signupRequest.getPassword2())) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("signup Passwords do not match", null));
		}

		if (userService.existsByUserId(signupRequest.getUserId()) > 0) {
			return ResponseEntity.badRequest()
					.body(new CommonResponse<>("This ID is already in use. Please choose another one", null));
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
	 * @created 2025-09-24 회원가입 시 아이디 중복체크
	 */
	@PostMapping("/users/id/verify")
	public ResponseEntity<CommonResponse<?>> checkId(@RequestParam("userId") String userId,
			HttpServletRequest request) {
		if (userService.existsByUserId(userId) > 0) {
			return ResponseEntity.badRequest()
					.body(new CommonResponse<>("This ID is already in use. Please choose another one", userId));
		} else {
			return ResponseEntity.ok(new CommonResponse<>("check userId success", userId));
		}
	}

	/**
	 * @param findUserIdRequest
	 * @param request
	 * @return CommonResponse
	 * @author kys
	 * @created 2025-09-24 사용자 아이디를 찾기 위해 사용자명과 이메일 입력 -> 검증 후 사용자 아이디 반환
	 * 
	 * @modified 2025-09-30 응답에 사용자 id, 계정 생성일, 비밀번호 수정일 추가
	 */
	@PostMapping("/users/id")
	public ResponseEntity<CommonResponse<?>> findUserId(@Valid @RequestBody FindUserIdRequest findUserIdRequest,
			HttpServletRequest request) {

		String name = findUserIdRequest.getName();
		String email = findUserIdRequest.getEmail();

		String verifiedEmail = (String) request.getSession().getAttribute("verifiedEmail");
		if (verifiedEmail == null || !verifiedEmail.equals(email)) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("Email not verified", null));
		}

		if (userService.existsByUserName(name) <= 0) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("User name not found", null));
		}

		try {
			String userId = userService.findUserIdByNameAndEmail(name, email);
			request.getSession().setAttribute("findUserId", userId); // userId session에 저장 (pw 재설정을 위해)

			// 응답에 사용자 id, 계정 생성일, 비밀번호 수정일 추가
			Users checkUserInfo = userService.findByUserId(userId);
			LocalDateTime createdDate = checkUserInfo.getCreatedAt();
			LocalDateTime changedPw = checkUserInfo.getPwdChangedAt();

			Map<String, Object> userInfoMap = new HashMap<>();
			userInfoMap.put("userId", userId);
			userInfoMap.put("createdDate", createdDate);
			userInfoMap.put("changedPw", changedPw);

			return ResponseEntity.ok(new CommonResponse<>("Find userId success", userInfoMap));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(new CommonResponse<>(e.getMessage(), null));
		}
	}

	/**
	 * @param resetPasswordRequest
	 * @param request
	 * @return ResponseEntity<CommonResponse<?>>
	 * @author kys
	 * @created 2025-09-25 아이디 찾기 후 사용자 비밀번호 재설정 가능
	 */
	@PostMapping("/users/password")
	public ResponseEntity<CommonResponse<?>> resetPassword(
			@Valid @RequestBody ResetPasswordRequest resetPasswordRequest, HttpServletRequest request) {
		String userId = (String) request.getSession().getAttribute("findUserId");

		if (!resetPasswordRequest.getPassword1().equals(resetPasswordRequest.getPassword2())) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("reset Passwords do not match", null));
		}

		Users resetPw = userService.resetPassword(userId, resetPasswordRequest.getPassword1());
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new CommonResponse<>("updated reset password info success", resetPw));

	}

	/**
	 * @param updateRequestJson
	 * @param profileImage
	 * @param request
	 * @return ResponseEntity<CommonResponse<?>>
	 * @throws IOException
	 * @author kys
	 * @created 2025-10-02
	 * 회원 정보 수정 (비밀번호, 이메일, 이름, 닉네임, 자기소개, 프로필 이미지) 이메일 정보가
	 * 입력된 이메일과 다를 경우에만 이메일 인증 진행 비밀번호 일치 여부 확인 프로필 이미지 blob 타입으로 저장
	 */
	@PutMapping(value = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CommonResponse<?>> updateUser(
			@RequestPart(value = "updateRequest", required = false) String updateRequestJson,
			@RequestPart(value = "profileImage", required = false) @Schema(type = "string", format = "binary", nullable = true, description = "프로필 이미지 (선택)") MultipartFile profileImage,
			HttpServletRequest request) throws IOException {

		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		Users updatedUser = userService.findByUserId(userId);

		UserUpdateRequest updateRequest = new ObjectMapper().readValue(updateRequestJson, UserUpdateRequest.class);

		if (StringUtils.hasText(updateRequest.getEmail()) && !updateRequest.getEmail().equals(updatedUser.getEmail())) {
			if (!emailVerificationService.isVerified("updateEmail", updateRequest.getEmail())) {
				return ResponseEntity.badRequest()
						.body(new CommonResponse<>("update Email verification required", null));
			}
		}

		if (profileImage != null && !profileImage.isEmpty()) {
			updateRequest.setProfileImage(profileImage.getBytes());
		}
		if (!updateRequest.getPassword1().equals(updateRequest.getPassword2())) {
			return ResponseEntity.badRequest().body(new CommonResponse<>("update Passwords do not match", null));
		}

		updatedUser = userService.requestUpdateUser(userId, updateRequest);

		if (updatedUser != null && emailVerificationService.isVerified("updateEmail", updateRequest.getEmail())) {
			emailVerificationService.clearVerified("updateEmail", updateRequest.getEmail());
		}
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new CommonResponse<>("updated user info success", updatedUser));

	}
	
	/**
	 * @param request
	 * @return ResponseEntity<CommonResponse<?>>
	 * @author kys
	 * @created 2025-10-02
	 * 현재 로그인한 사용자 정보 조회
	 */
	@GetMapping("/users")
	public ResponseEntity<CommonResponse<?>> getUserInfo (HttpServletRequest request) {
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		Users getUser = userService.findByUserId(userId);
		
			return ResponseEntity.ok()
					.body(new CommonResponse<>("get current user info", getUser));
		
	}
	
}
