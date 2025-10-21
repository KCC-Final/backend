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
	 */
	SseEmitter subscribe(String userId);

	/**
	 * @param request
	 * @throws IOException
	 */
	void sendNotification(NotificationRequest request) throws IOException;

	/**
	 * @param userId
	 * @return
	 */
	List<Alerts> getNotificationList(String userId);

	/**
	 * @param request
	 * @return
	 */
	int updateAlertsCheckStatus(NotificationUpdateRequest request);

	/**
	 * @param userId
	 * @return
	 */
	int getUnreadNotificationCount(String userId);


	/**
	 * @param userId
	 * @param alertId
	 * @return
	 */
	Alerts getNotificationById(String userId,int alertId);
	
	
	
}
