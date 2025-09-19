package com.kcc.groo.user.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kcc.groo.user.dao.IUsersRepository;
import com.kcc.groo.user.data.model.Users;

@Service
public class UserService implements IUserService{
	
	@Autowired
	IUsersRepository usersRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

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
	public Users insertUser(String userId, String rawPassword, String email, String nickName, char gender, String name,
			LocalDate birth) {
		// TODO Auto-generated method stub
		
		if(usersRepository.existsByUserId(userId) > 0) {
		throw new IllegalArgumentException("already exist id");	
		}
		
		Users user = new Users();
		user.setUserId(userId);
		user.setPassword(passwordEncoder.encode(rawPassword));
		user.setEmail(email);
		user.setNickname(nickName);
		user.setGender(gender);
		user.setName(name);
		user.setBirth(birth);
		
		int result =  usersRepository.insertUser(user);
		
		if (result > 0) {
			return user;
		} else {
			throw new RuntimeException("failed signup");
		}
	}

	@Override
	public List<Users> selectAllUserId() {
		// TODO Auto-generated method stub
		return usersRepository.selectAllUserId();
	}

}
