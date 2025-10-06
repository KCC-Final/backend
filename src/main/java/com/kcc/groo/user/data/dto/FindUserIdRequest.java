package com.kcc.groo.user.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindUserIdRequest {
	
	@NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(min = 2, max = 30, message = "이름은 2~30자여야 합니다.")
	private String name;
	
	@NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
	private String email;

}
