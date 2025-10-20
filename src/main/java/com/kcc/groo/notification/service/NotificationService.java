package com.kcc.groo.notification.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kcc.groo.challenge.dao.IBadgeRepository;
import com.kcc.groo.dashboard.dao.IDashboardRepository;
import com.kcc.groo.notification.dao.INotificationRepository;
import com.kcc.groo.notification.data.dto.NotificationRequest;
import com.kcc.groo.notification.data.dto.NotificationUpdateRequest;
import com.kcc.groo.notification.data.model.Alerts;
import com.kcc.groo.review.dao.ICommentRepository;
import com.kcc.groo.review.dao.IReviewRepository;
import com.kcc.groo.user.dao.IFollowsRepository;
import com.kcc.groo.user.dao.IUsersRepository;

@Service
public class NotificationService implements INotificationService{
	
	@Autowired
	INotificationRepository notificationRepository;
	@Autowired
	IUsersRepository usersRepository;
	@Autowired
	IFollowsRepository followsRepository; //follow
	@Autowired
	ICommentRepository commentRepository; //comment - review
	@Autowired
	IDashboardRepository dashboardRepository; //date
	@Autowired
	IBadgeRepository badgeRepository; //challenge
	@Autowired
	IReviewRepository reviewRepository;//like
	
	@Override
	public Alerts insertNotification(String userId, NotificationRequest notificationRequest) {
		// TODO Auto-generated method stub
		Alerts newAlerts = new Alerts();
		
		if (!StringUtils.hasText(userId)) {
			throw new IllegalArgumentException("can not found account");
		}
		if (!StringUtils.hasText(notificationRequest.getType())) {
			throw new IllegalArgumentException("needs to insert type");
		}
		if (!StringUtils.hasText(notificationRequest.getContent())) {
			throw new IllegalArgumentException("needs to insert content");
		}
		if (!StringUtils.hasText(notificationRequest.getSenderType())) {
			throw new IllegalArgumentException("needs to insert sender type");
		}
		newAlerts.setType(notificationRequest.getType());
		newAlerts.setContent(notificationRequest.getContent());
		newAlerts.setSenderType(notificationRequest.getSenderType());
		newAlerts.setSenderId(notificationRequest.getSenderId());
		newAlerts.setDetailSenderId(notificationRequest.getDetailSenderId());
		newAlerts.setUserId(userId);
		newAlerts.setSenderUserId(notificationRequest.getSenderUserId());
		
		int result = notificationRepository.insertNotification(newAlerts);
		
		if (result <= 0) {
			throw new RuntimeException("failed alert");
		}
		Alerts alertInfo = notificationRepository.getAlerts(userId, newAlerts.getAlertId());
		
		return alertInfo;
	}
	
	@Override
	public Alerts updateAlertsCheckStatus(String userId, NotificationUpdateRequest notificationUpdateRequest) {
		return null;
	}

	@Override
	public Alerts updateAlertsCheckStatusList(String userId, int alertId, List<Boolean> statusList) {
		return null;
	}

	@Override
	public List<Alerts> getNotificationList(String userId) {
		return null;
	}

	@Override
	public Alerts getAlerts(String userId, int alertId) {
		return notificationRepository.getAlerts(userId, alertId);
	}


}
