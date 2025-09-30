package com.kcc.groo.user.data.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {

    private String password1;

    private String password2;

    private String email;

    @Size(min = 2, max = 15, message = "닉네임은 2~15자여야 합니다.")
    private String nickname;

    @Size(min = 2, max = 30, message = "이름은 2~30자여야 합니다.")
    private String name;

    @Schema(type = "string", format = "binary")
    private MultipartFile profileImageFile; //multipartFile
    private byte[] profileImage; //db save
   
	private String introduction;

}