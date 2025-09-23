package com.kcc.groo.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kcc.groo.user.data.model.Users;

@Repository
@Mapper
public interface IUsersRepository {
	
	Users selectUserByUserId (String userId);
	int insertUser (Users user);
	List<Users> selectAllUserId(); //select all userId
	int existsByUserId (String userId); //check userId in db
	int updateEmailVerified(@Param("email") String email, @Param("emailVerified") boolean emailVerified);
}
