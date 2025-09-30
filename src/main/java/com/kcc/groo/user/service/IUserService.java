package com.kcc.groo.user.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kcc.groo.user.data.dto.SignupRequest;
import com.kcc.groo.user.data.dto.UpdateRequest;
import com.kcc.groo.user.data.model.Users;

public interface IUserService {

	
	/**
	 * @param userId
	 * @param password
	 * @return
	 * @author kys
	 * @created 2025-09-15
	 * 회원 로그인 처리
	 */
	Users loginUser (String userId, String password);
	
	/**
	 * @param signupRequest
	 * @return
	 * @author kys
	 * @created 2025-09-23
	 * 신규 회원 가입 처리 / 아이디 중복 여부 확인 / 비밀번호 암호화 후 저장
	 */
	Users requestInsertUser (SignupRequest signupRequest);
	

	/**
	 * @param purpose
	 * @param email
	 * @param code
	 * @return
	 * @author kys
	 * @created 2025-09-23
	 */
	boolean confirmEmail (@Param("purpose") String purpose, @Param("email") String email, @Param("code") String code);
	
	/**
	 * @return
	 * @author kys
	 * @created 2025-09-15
	 * 모든 회원 목록 조회
	 */
	List<Users> selectAllUserId();
	
	/**
	 * @param name
	 * @return
	 * @author kys
	 * @created 2025-09-24
	 * 특정 이름을 가진 회원 확인
	 */
	int existsByUserName (@Param("name") String name);
	
	/**
	 * @param name
	 * @param email
	 * @return
	 * @author kys
	 * @created 2025-09-24
	 * 이름과 이메일로 사용자 아이디 조회
	 */
	String findUserIdByNameAndEmail (@Param("name") String name, @Param("email") String email); //get userId
	
	
	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-09-25
	 * db에 사용자가 존재하는지 조회
	 */
	int existsByUserId (@Param("userId") String userId);

	/**
	 * @param resetPasswordRequest
	 * @return
	 * @author kys
	 * @created 2025-09-25
	 * 비밀번호 재설정
	 */
	Users resetPassword(@Param("userId") String userId, @Param("rawPassword") String rawPassword);
	
	/**
	 * @param updateRequest
	 * @return
	 * @author kys
	 * @created 2025-09-30
	 * 이메일 인증 필요 없을 경우의 회원 정보 수정
	 */
	Users updateUserWithoutEmailVerified (UpdateRequest updateRequest);
	
	/**
	 * @param updateRequest
	 * @return
	 * @author kys
	 * @created 2025-09-30
	 * 이메일 인증이 필요한 경우의 회원 정보 수정
	 */
	Users updateUserWithEmailVerified (UpdateRequest updateRequest);
	
	
	
}
