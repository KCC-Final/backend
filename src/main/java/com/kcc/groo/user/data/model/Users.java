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
	
	private String userId;
	private String password;
	private String email;
	private String nickname;
	private String profileImage;
	private String introduction;
	private char gender;
	private LocalDate birth;
	private LocalDateTime createdAt;

}
