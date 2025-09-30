package com.kcc.groo.user.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
	
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_\\-+=]{8,20}$",
        message = "비밀번호는 대/소문자, 숫자를 포함한 8~20자여야 합니다."
    )
    private String password1;

    @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
    private String password2;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @Size(min = 2, max = 15, message = "닉네임은 2~15자여야 합니다.")
    private String nickname;

    @Size(min = 2, max = 30, message = "이름은 2~30자여야 합니다.")
    private String name;

    private byte[] profileImage;
   
	private String introduction;

}