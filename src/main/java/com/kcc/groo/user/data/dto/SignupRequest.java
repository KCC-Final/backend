package com.kcc.groo.user.data.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
	
	String userId; //db에서 중복체크 해야함 -> service
	String password1; //일치 여부 확인 -> service
	String password2;
	String email;
	String nickName;
	char gender;
	String name;
	LocalDate birth;
	boolean checkPrivacy;
	boolean checkService;
	boolean emailVerified;

}
