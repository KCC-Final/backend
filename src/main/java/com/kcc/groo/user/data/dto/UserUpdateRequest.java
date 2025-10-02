package com.kcc.groo.user.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
	
	private String password1;
	private String password2;
	private String nickname;
	private String email;
	private byte[] profileImage;
	private String introduction;
	private String name;
	
}
