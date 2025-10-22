package com.kcc.groo.notification.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.kcc.groo.notification.data.dto.NotificationRequest;
import com.kcc.groo.notification.data.dto.NotificationUpdateRequest;
import com.kcc.groo.notification.data.model.Alerts;

public interface INotificationService {
	

	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-21
	 * 
	 */
	SseEmitter subscribe(String userId);

	/**
	 * @param request
	 * @throws IOException
	 * @author kys
	 * @created 2025-10-21
	 * 알림 전송
	 */
	void sendNotification(NotificationRequest request) throws IOException;

	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-21
	 * 알림 목록 조회
	 */
	List<Alerts> getNotificationList(String userId);

	/**
	 * @param request
	 * @return
	 * @author kys
	 * @created 2025-10-22
	 * 알림 단건 읽음처리
	 */
	int updateAlertsCheckStatus(String userId, int alertId, NotificationUpdateRequest request);

	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-22
	 * 읽지 않은 알림 수
	 */
	int getUnreadNotificationCount(String userId);


	/**
	 * @param userId
	 * @param alertId
	 * @return
	 * @author kys
	 * @created 2025-10-22
	 * 알림 정보 조회
	 */
	Alerts getNotificationById(String userId,int alertId);
	
	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-22
	 * 알림 아이디 리스트
	 */
	List<Integer> alertIdList (String userId, Boolean alertsCheckStatus);

	
	/**
	 * @param userId
	 * @param alertIdList
	 * @return
	 * @author kys
	 * @created 2025-10-22
	 * 알림 전체 읽음 처리
	 */
	int readAllAlerts(String userId, List<Integer> alertIdList);
	
	
}
