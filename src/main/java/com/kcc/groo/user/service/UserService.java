package com.kcc.groo.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kcc.groo.user.dao.IUsersRepository;
import com.kcc.groo.user.data.dto.SignupRequest;
import com.kcc.groo.user.data.model.Users;

@Service
public class UserService implements IUserService{

	@Autowired
	IUsersRepository usersRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    
    @Autowired
    JavaMailSender javaMailSender;

	/**
	 * @param userId
	 * @param password
	 * @return
	 */
	@Override
	public Users loginUser (String userId, String password) {
		// TODO Auto-generated method stub
		Users user = usersRepository.selectUserByUserId(userId);
		
		if(user == null) {
			throw new IllegalArgumentException("can not found account");
		}
		if (!user.isEmailVerified()) {
			throw new IllegalStateException("need to verified email");
		}
		if(!passwordEncoder.matches(password, user.getPassword())) {
	        throw new IllegalArgumentException("password does not match");
	    }
		
		return user;
	}

	/**
	 * @param userId
	 * @param rawPassword
	 * @param email
	 * @param nickName
	 * @param gender
	 * @param name
	 * @param birth
	 * @return
	 */
	@Override
	public Users insertUser(SignupRequest signupRequest) {
		// TODO Auto-generated method stub
		
		if(usersRepository.existsByUserId(signupRequest.getUserId()) > 0) {
		throw new IllegalArgumentException("already exist id");	
		}
		
		int result = 0;
		
		Users user = new Users();
		user.setUserId(signupRequest.getUserId());
		
		if (signupRequest.getPassword1().equals(signupRequest.getPassword2())) {
			user.setPassword(passwordEncoder.encode(signupRequest.getPassword1()));
		}
		user.setEmail(signupRequest.getEmail());
		user.setNickName(signupRequest.getNickName());
		user.setGender(signupRequest.getGender());
		user.setName(signupRequest.getName());
		user.setBirth(signupRequest.getBirth());
		user.setCheckPrivacy(signupRequest.isCheckPrivacy());
		user.setCheckService(signupRequest.isCheckService());
		user.setEmailVerified(false);
		
		result =  usersRepository.insertUser(user);
		
		if (result > 0) {
			return user;
		} else {
			throw new RuntimeException("failed signup");
		}
	}

	@Override
	public String verificationEmail(String email) {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	@Override
	public List<Users> selectAllUserId() {
		// TODO Auto-generated method stub
		return usersRepository.selectAllUserId();
	}



}
