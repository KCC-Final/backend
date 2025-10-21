package com.kcc.groo.notification.data.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerts {
	
	private int alertId; //pk
	private String type; //follow, like, comment, badge -> 알림의 행위
	
	//ex: {senderUserId}님이 {senderId: review_id join title}게시물{senderType:review}{review_id join title}에 {type: comment}를 남겼습니다. 시간: {sendAt} / 수신자: {userId} / 이동할 페이지:{detailSenderId: comment_id}
	//ex: {senderUserId}님이 {senderId: review_id join title}게시물{senderType:review}{review_id join title}에 {type: like}를 남겼습니다. 시간: {sendAt} / 수신자: {userId} / 이동할 페이지:{senderId: review_id}
	//ex: {userId}님이 {type: badge}{detailSenderId: badge_id}를 획득하였습니다!{senderId: users} 시간: {sendAt} / 수신자: {userId} / 이동할 페이지:{detailSenderId: badge_id} / 이동할 페이지:{senderId: my dashboard}
	//ex: {senderUserId}님이 {userId}님을 {type:follow}하기 시작했습니다!{senderType:follow id} 시간: {sendAt} / 수신자: {userId} / 이동할 페이지:{senderUserId 피드} 
	private String content; 
	
	private LocalDateTime sendAt; //전송 시간
	private String senderType; // -> sender Id가 속해있는 대분류: review, follow, likes, users -> 알림의 출처
	private int senderId; //review id, follow id, userId (get: dash board / feed)
	private int detailSenderId; // comment,badge id
	private String userId; //수신자
	private String senderUserId; //발신자
	private boolean alertsCheckStatus;

}
