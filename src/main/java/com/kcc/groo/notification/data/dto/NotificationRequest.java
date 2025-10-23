package com.kcc.groo.notification.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
	private String type; //follow, like, comment, badge -> 알림의 행위
	private String senderType; // -> sender Id가 속해있는 대분류: review, follow, likes, users -> 알림의 출처
	private int senderId; //review id, follow id, userId (get: dash board / feed)
	private String content;
	private int detailSenderId; // comment,badge id
	private String userId; //수신자
	private String senderUserId; //발신자


}
