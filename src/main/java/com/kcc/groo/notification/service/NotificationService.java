package com.kcc.groo.notification.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
	
	 private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	
	 /**
     * 실시간 구독 연결
     */
    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(1000L * 60 * 5); // 5분 타임아웃
        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError((e) -> emitters.remove(userId));

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }
	
	@Override
	@Transactional
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
		
		 // 실시간 전송
        SseEmitter emitter = emitters.get(newAlerts.getUserId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("alert")
                        .data(newAlerts));
            } catch (IOException e) {
                emitters.remove(newAlerts.getUserId());
            }
        }
        
		Alerts alertInfo = notificationRepository.getAlerts(userId, newAlerts.getAlertId());
		
		return alertInfo;
	}
	
	@Override
	public Alerts updateAlertsCheckStatus(String userId, NotificationUpdateRequest notificationUpdateRequest) {
		Alerts alert = new Alerts();
        alert.setAlertId(notificationUpdateRequest.getAlertId());
        alert.setUserId(notificationUpdateRequest.getUserId());
        alert.setAlertsCheckStatus(notificationUpdateRequest.isAlertsCheckStatus());
        notificationRepository.updateAlertsCheckStatus(alert);
        
        Alerts alertInfo = notificationRepository.getAlerts(userId, notificationUpdateRequest.getAlertId());
		
		return alertInfo;
	}

	@Override
	public Alerts updateAlertsCheckStatusList(String userId, int alertId, List<Boolean> statusList) {
		return null;
	}

	@Override
	public List<Alerts> getNotificationList(String userId) {
		return notificationRepository.getNotificationList(userId);
	}

	@Override
	public Alerts getAlerts(String userId, int alertId) {
		return notificationRepository.getAlerts(userId, alertId);
	}


}
