package com.kcc.groo.notification.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kcc.groo.notification.data.dto.NotificationRequest;
import com.kcc.groo.notification.data.dto.NotificationUpdateRequest;
import com.kcc.groo.notification.data.model.Alerts;

public interface INotificationService {

	Alerts insertNotification(String userId, NotificationRequest notificationRequest);

	Alerts updateAlertsCheckStatus(String userId, NotificationUpdateRequest notificationUpdateRequest);

	/**
	 * @param userId
	 * @param alertId
	 * @param statusList
	 * @return
	 * @author kys
	 * @created 2025-10-17 알림 확인 컬럼 여러 건 업데이트
	 */
	Alerts updateAlertsCheckStatusList(String userId, int alertId, List<Boolean> statusList);

	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-17 알림 확인 목록
	 */
	List<Alerts> getNotificationList(String userId);
	
	/**
	 * @param userId
	 * @param alertId
	 * @return
	 * @author kys
	 * @created 2025-10-17
	 * 알림 상세 정보
	 */
	Alerts getAlerts(String userId, int alertId);

}
