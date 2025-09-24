package com.kcc.groo.user.service;

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
		
		if (result > 0) {
			return newUser;
		} else {
			throw new RuntimeException("failed signup");
		}
	}

	
	@Override
	public boolean confirmEmail(String purpose, String email, String code) {
		return emailVerificationService.verifyCode(purpose, email, code);
	}
	
	public int updatedEmailVerified (String purpose,String email, boolean verified, String code) {
		if (!confirmEmail("signup", email, code)) {
			throw new IllegalArgumentException("failed verification");
		}
		
		return usersRepository.updateEmailVerified(email, verified);
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



	
	



}
