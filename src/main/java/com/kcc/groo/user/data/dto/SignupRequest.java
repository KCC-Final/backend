package com.kcc.groo.user.data.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
	
	@NotBlank(message = "아이디는 필수 입력값입니다.")
    @Size(min = 4, max = 20, message = "아이디는 4~20자여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "아이디는 영문과 숫자만 사용할 수 있습니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
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

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Size(min = 2, max = 15, message = "닉네임은 2~15자여야 합니다.")
    private String nickname;

    @NotNull(message = "성별은 필수 선택값입니다.")
    private Character gender;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(min = 2, max = 30, message = "이름은 2~30자여야 합니다.")
    private String name;

    @NotNull(message = "생년월일은 필수 입력값입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birth;
    
	boolean checkPrivacy;
	boolean checkService;

}
