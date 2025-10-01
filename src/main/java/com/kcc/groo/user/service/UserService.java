package com.kcc.groo.user.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.user.dao.IUsersRepository;
import com.kcc.groo.user.data.dto.SignupRequest;
import com.kcc.groo.user.data.dto.UserUpdateRequest;
import com.kcc.groo.user.data.model.Users;

@Service
@Transactional
public class UserService implements IUserService {

	private final JwtTokenProvider jwtTokenProvider;

	@Autowired
	IUsersRepository usersRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	EmailVerificationService emailVerificationService;

	@Autowired
	MailService mailService;

	UserService(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public Users loginUser(String userId, String password) {
		// TODO Auto-generated method stub
		Users user = usersRepository.selectUserByUserId(userId);

		if (user == null) {
			throw new IllegalArgumentException("can not found account");
		}
		if (!user.isEmailVerified()) {
			throw new IllegalStateException("need to verified email");
		}
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("password does not match");
		}

		return user;
	}

	@Override
	public Users requestInsertUser(SignupRequest signupRequest) {
		Users newUser = new Users();
		newUser.setUserId(signupRequest.getUserId());
		newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword1()));
		newUser.setEmail(signupRequest.getEmail());
		newUser.setNickname(signupRequest.getNickname());
		newUser.setGender(signupRequest.getGender());
		newUser.setName(signupRequest.getName());
		newUser.setBirth(signupRequest.getBirth());
		newUser.setCheckPrivacy(signupRequest.isCheckPrivacy());
		newUser.setCheckService(signupRequest.isCheckService());
		// newUser.setEmailVerified(true);

		int result = usersRepository.insertUser(newUser);

		if (result > 0) {
			return usersRepository.selectUserByUserId(newUser.getUserId());
		} else {
			throw new RuntimeException("failed signup");
		}
	}

	@Override
	public boolean confirmEmail(String purpose, String email, String code) {
		return emailVerificationService.verifyCode(purpose, email, code);
	}

	@Override
	public String findUserIdByNameAndEmail(String name, String email) {
		String userId = usersRepository.findUserIdByNameAndEmail(name, email);

		if (userId == null) {
			throw new IllegalArgumentException("can not found name or email");
		}
		return userId;
	}

	@Override
	public List<Users> selectAllUserId() {
		// TODO Auto-generated method stub
		return usersRepository.selectAllUserId();
	}

	@Override
	public int existsByUserName(String name) {
		// TODO Auto-generated method stub
		return usersRepository.existsByUserName(name);
	}

	@Override
	public int existsByUserId(String userId) {
		// TODO Auto-generated method stub
		return usersRepository.existsByUserId(userId);
	}

	@Override
	public Users resetPassword(String userId, String rawPassword) {
		Users user = usersRepository.selectUserByUserId(userId);
		int result = usersRepository.resetPassword(userId, passwordEncoder.encode(rawPassword));
		if (result > 0) {
			return user;
		} else {
			throw new RuntimeException("failed update Password");
		}
	}

	public Users findByUserId(String userId) {
		return usersRepository.selectUserByUserId(userId);
	}

	@Override
	public Users requestUpdateUser(String userId, UserUpdateRequest updateRequest) { // password nickname email
																						// profileImage introduction
																						// name

		// set userId
		Users updateUser = usersRepository.selectUserByUserId(userId);

		// check pw
		if (!updateUser.getPassword().equals(updateRequest.getPassword1())) { // 기존 비밀번호와 password1 다를 경우
			if (StringUtils.hasText(updateRequest.getPassword1())
					&& StringUtils.hasText(updateRequest.getPassword2())) { // null or ""
				updateUser.setPassword(passwordEncoder.encode(updateRequest.getPassword1())); // pw 1 encoding
				updateUser.setPwdChangedAt(LocalDateTime.now()); // set pwd change date
			}
		}

		// email
		if (!updateUser.getEmail().equals(updateRequest.getEmail())) { // 기존 이메일과 새로 입력된 이메일이 다를 경우
			if (StringUtils.hasText(updateRequest.getEmail())) { // null or ""
				updateUser.setEmailVerified(false); // verified 초기화
			} // else == true 유지
		}

		// others
		if (StringUtils.hasText(updateRequest.getNickname())) { // null or ""
			updateUser.setNickname(updateRequest.getNickname());
		}
		if (StringUtils.hasText(updateRequest.getIntroduction())) { // null or ""
			updateUser.setIntroduction(updateRequest.getIntroduction());
		}
		if (StringUtils.hasText(updateRequest.getName())) { // null or ""
			updateUser.setName(updateRequest.getName());
		}

		if (updateRequest.getProfileImage() != null && updateRequest.getProfileImage().isEmpty()) { // null or ""
			try {
				updateUser.setProfileImage(updateRequest.getProfileImage().getBytes()); // byte로 변환해 저장ㄴ
			} catch (IOException e) {
				throw new RuntimeException("failed to convert profile image");
			}
		}

		int result = usersRepository.updateUser(updateUser);

		if (result > 0) {
			return usersRepository.selectUserByUserId(userId);
		} else {
			throw new RuntimeException("failed signup");
		}

	}
}