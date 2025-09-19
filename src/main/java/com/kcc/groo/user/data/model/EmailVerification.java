package com.kcc.groo.user.data.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {
	
	private int verificationId; //pk
	private String userId; //fk
	private String token; //verificatin token
	private LocalDateTime expires_at; //만료 시간 (5분)
	private boolean isUsed; //토큰 사용 여부
	private LocalDateTime createdAt; //토큰 생성일

}
