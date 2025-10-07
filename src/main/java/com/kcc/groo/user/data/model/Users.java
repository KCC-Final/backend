package com.kcc.groo.user.data.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
	
	private String userId; //pk
	private String password; //비밀번호
	private String email; //이메일
	private String nickname; //별명
	private byte[] profileImage; //프로필 이미지
	private String introduction; //자기소개
	private char gender; //성별
	private String name; //이름
	private LocalDate birth; //생년월일
	private LocalDateTime createdAt; //계정 생성일
	private boolean withdrawalStatus; //탈퇴 상태
	private LocalDateTime withdrawalDate; //탈퇴일
	private LocalDateTime pwdChangedAt; //비밀변호 변경일
	private boolean checkPrivacy; //개인정보 이용 동의
	private boolean checkService; //서비스 이용 동의
	private boolean emailVerified;

}
