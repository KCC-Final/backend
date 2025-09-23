package com.kcc.groo.user.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    EmailVerificationService emailVerificationService;
    
    @Autowired
    MailService mailService;

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

	
	@Override
	public Users requestInsertUser(SignupRequest signupRequest) {
		// TODO Auto-generated method stub
		
		
		if(usersRepository.existsByUserId(signupRequest.getUserId()) > 0) {
		throw new IllegalArgumentException("already exist id");	
		}
		
		Users newUser = new Users();
	    newUser.setUserId(signupRequest.getUserId());
	    newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword1()));
	    newUser.setEmail(signupRequest.getEmail());
	    newUser.setNickName(signupRequest.getNickName());
	    newUser.setGender(signupRequest.getGender());
	    newUser.setName(signupRequest.getName());
	    newUser.setBirth(signupRequest.getBirth());
	    newUser.setCheckPrivacy(signupRequest.isCheckPrivacy());
	    newUser.setCheckService(signupRequest.isCheckService());
	    newUser.setEmailVerified(true);
		
		int result =  usersRepository.insertUser(newUser);
		//String email = signupRequest.getEmail();
		
		if (result > 0) {
			/*
			 * String code = emailVerificationService.createVerificationCode(email);
			 * mailService.sendVerificationEmail(email, code);
			 */
			return newUser;
		} else {
			throw new RuntimeException("failed signup");
		}
	}

	
	@Override
	public boolean confirmEmail(String email, String code) {
		// TODO Auto-generated method stub
		boolean verified = emailVerificationService.verifyCode(email, code);
		if (!verified) {
			return false;
		}
		
		int updated = usersRepository.updateEmailVerified(email, verified);
		
		return updated > 0;
	}

	@Override
	public List<Users> selectAllUserId() {
		// TODO Auto-generated method stub
		return usersRepository.selectAllUserId();
	}



}
