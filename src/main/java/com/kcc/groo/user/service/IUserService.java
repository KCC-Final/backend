package com.kcc.groo.user.service;

import com.kcc.groo.user.data.model.Users;

public interface IUserService {

	Users loginUser (String userId, String password);
	
}
