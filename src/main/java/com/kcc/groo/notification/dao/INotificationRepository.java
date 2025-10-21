package com.kcc.groo.notification.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.kcc.groo.notification.data.model.Alerts;

@Repository
@Mapper
public interface INotificationRepository {
	
	/**
	 * @param alert
	 * @return
	 * @author kys
	 * @created 2025-10-17
	 * 알림 생성
	 */
	int insertNotification (Alerts alert);
	
	/**
	 * @param status
	 * @return
	 * @author kys
	 * @created 2025-10-17
	 * 알림 확인 컬럼 단건 업데이트
	 */
	 int updateAlertsCheckStatus(@Param("alertId") int alertId,
             @Param("alertsCheckStatus") Boolean alertsCheckStatus);
	
	/**
	 * @param userId
	 * @param alertId
	 * @param statusList
	 * @return
	 * @author kys
	 * @created 2025-10-17
	 * 알림 확인 컬럼 여러 건 업데이트
	 */
	int updateAlertsCheckStatusList(@Param("userId") String userId, @Param("alertId") int alertId, @Param("statusList") List<Boolean> statusList);
	
	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-17
	 * 알림 확인 목록
	 */
	List<Alerts> getNotificationList(@Param("userId") String userId);
	
	/**
	 * @param userId
	 * @param alertId
	 * @return
	 * @author kys
	 * @created 2025-10-17
	 * 알림 상세
	 */
	Alerts getAlerts(@Param("userId") String userId, @Param("alertId") int alertId);

	/**
	 * @param userId
	 * @return
	 * @author kys
	 * @created 2025-10-21
	 * 확인하지 않은 알림 개수 확인
	 */
	int countUnreadNotifications(String userId);
}
