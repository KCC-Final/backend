package com.kcc.groo.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.user.dao.IUsersRepository;
import com.kcc.groo.user.data.dto.SignupRequest;
import com.kcc.groo.user.data.dto.UpdateRequest;
import com.kcc.groo.user.data.model.Users;

@Service
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
		newUser.setEmailVerified(true);

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

	@Override
	public Users updateUserWithoutEmailVerified (UpdateRequest updateRequest) {
		return null;
	}

	@Override
	public Users updateUserWithEmailVerified (UpdateRequest updateRequest) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getUserIdInToken (String token) {
		
		Authentication authentication = jwtTokenProvider.getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "";
	}
}