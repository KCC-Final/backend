package com.kcc.groo.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcc.groo.user.data.model.Users;

@Repository
@Mapper
public interface IUsersRepository {
	
	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-09-15
	 * 주어진 userId로 단일 회원 정보를 조회
	 */
	Users selectUserByUserId (String userId);
	
	/**
	 * @param user
	 * @return
	 * @author kys
	 * @created 2025-09-23
	 * 새로운 회원 정보 db에 저장
	 */
	int insertUser (Users user); //save user
	
	/**
	 * @return
	 * @author kys
	 * @created 2025-09-15
	 * 모든 회원의 userId 목록을 조회
	 */
	List<Users> selectAllUserId();
	
	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-09-23
	 * 회원가입 시 중복 아이디 체크
	 */
	int existsByUserId (String userId);
	
	/**
	 * @param email
	 * @param emailVerified
	 * @return
	 * @author kys
	 * @created 2025-09-23
	 * 이메일 인증 성공 여부 업데이트
	 */
	int updateEmailVerified(@Param("email") String email, @Param("emailVerified") boolean emailVerified);
	
	/**
	 * @param name
	 * @param email
	 * @return
	 * @author kys
	 * @created 2025-09-24
	 * 이메일, 이름을 통해 회원의 아이디 조회
	 */
	String findUserIdByNameAndEmail (@Param("name") String name,@Param("email") String email);
	
	/**
	 * @param name
	 * @return
	 * @author kys
	 * @created 2025-09-24
	 * 특정 이름을 가진 회원이 존재하는지 확인
	 */
	int existsByUserName(String name);
	
	/**
	 * @param email
	 * @return
	 * @author kys
	 * @created 2025-09-24
	 * 특정 이메일이 DB에 등록되어 있는지 확인
	 */
	int existsByUserEmail(String email);
	

	/**
	 * @param userId
	 * @param password
	 * @return
	 * @author kys
	 * @created 2025-09-25
	 * 비밀번호 재설정
	 */
	int resetPassword(@Param("userId") String userId, @Param("password") String password); 
	
}