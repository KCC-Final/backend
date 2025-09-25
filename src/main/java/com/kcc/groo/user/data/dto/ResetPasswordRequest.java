package com.kcc.groo.user.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {
	
	String userId;
	
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_\\-+=]{8,20}$",
     message = "비밀번호는 대/소문자, 숫자를 포함한 8~20자여야 합니다." )
	String password1; //비밀번호 1
    
    @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
	String password2; //비밀번호 2
	

}
