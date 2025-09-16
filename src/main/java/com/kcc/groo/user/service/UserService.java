package com.kcc.groo.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kcc.groo.user.dao.IUsersRepository;
import com.kcc.groo.user.data.model.Users;

@Service
public class UserService implements IUserService{
	
	@Autowired
	IUsersRepository usersRepository;

	@Override
	public Users loginUser (String userId, String password) {
		// TODO Auto-generated method stub
		Users user = usersRepository.selectUserByUserId(userId);
		
		if(user == null) {
			throw new IllegalArgumentException("can not found account");
		}
		
		return user;
	}

}
