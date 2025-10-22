package com.kcc.groo.notification.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.kcc.groo.notification.NotificationFormatter;
import com.kcc.groo.notification.dao.EmitterRepository;
import com.kcc.groo.notification.dao.INotificationRepository;
import com.kcc.groo.notification.data.dto.NotificationRequest;
import com.kcc.groo.notification.data.dto.NotificationUpdateRequest;
import com.kcc.groo.notification.data.model.Alerts;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService implements INotificationService {

    @Autowired
    private EmitterRepository emitterRepository;

    @Autowired
    private INotificationRepository notificationRepository;

    @Autowired
    private NotificationFormatter formatter;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1시간

    @Override
    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

        emitter.onCompletion(() -> {
            emitterRepository.delete(userId);
            log.info("SSE 연결 종료 userId={}", userId);
        });

        emitter.onTimeout(() -> {
            emitterRepository.delete(userId);
            log.info("SSE 타임아웃 userId={}", userId);
        });

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("SSE 연결 성공: userId=" + userId));
        } catch (IOException e) {
            emitterRepository.delete(userId);
            log.error("SSE 초기 연결 실패 userId={}", userId, e);
        }

        return emitter;
    }

    @Override
    public void sendNotification(NotificationRequest request) {
        try {
            // content 자동 생성
            String content = request.getContent();
            if (content == null || content.isEmpty()) {
                content = formatter.generateContent(
                        request.getType(),
                        request.getSenderUserId(),
                        request.getSenderType(),
                        request.getSenderId(),
                        request.getDetailSenderId(),
                        request.getUserId());
            }

            Alerts alert = new Alerts();
            alert.setType(request.getType());
            alert.setContent(content);
            alert.setSenderType(request.getSenderType());
            alert.setSenderId(request.getSenderId());
            alert.setDetailSenderId(request.getDetailSenderId());
            alert.setUserId(request.getUserId());
            alert.setSenderUserId(request.getSenderUserId());
            alert.setSendAt(LocalDateTime.now());
            alert.setAlertsCheckStatus(false);

            // DB 저장
            notificationRepository.insertNotification(alert);
            log.info("알림 저장 완료 userId={} / type={} / content={}",
                    alert.getUserId(), alert.getType(), alert.getContent());

            // 실시간 전송
            SseEmitter emitter = emitterRepository.get(alert.getUserId());
            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("alert")
                            .data(alert));
                    log.info("SSE 전송 완료 userId={}", alert.getUserId());
                } catch (IOException e) {
                    emitterRepository.delete(alert.getUserId());
                    log.error("SSE 전송 실패 userId={}", alert.getUserId(), e);
                }
            } else {
                log.info("활성 SSE 연결 없음 userId={}", alert.getUserId());
            }

        } catch (Exception e) {
            log.error("알림 처리 중 오류 발생 userId={}", request.getUserId(), e);
        }
    }


    @Override
    public List<Alerts> getNotificationList(String userId) {
        List<Alerts> list = notificationRepository.getNotificationList(userId);
        log.info("알림 조회 userId={} / count={}", userId, list != null ? list.size() : 0);
        return list;
    }


    @Override
    @Transactional
    public int updateAlertsCheckStatus(String userId, int alertId, NotificationUpdateRequest notificationUpdateRequest) {
    	
    	int updated = notificationRepository.updateAlertsCheckStatus(alertId, notificationUpdateRequest.getAlertsCheckStatus());
    	
    	if (updated == 0) {
    		log.warn("알림 상태 업데이트 실패: alertId={}, userId={}", alertId, userId);
    	} else {
    		log.info("알림 상태 업데이트 완료: alertId={}, userId={}", alertId, userId);
    	}
        return updated;
    }

	@Override
	public int getUnreadNotificationCount(String userId) {
		 int count = notificationRepository.countUnreadNotifications(userId);
		    log.info("읽지 않은 알림 개수 userId={} count={}", userId, count);
		    return count;
	}

	@Override
	public Alerts getNotificationById(String userId, int alertId) {
		// TODO Auto-generated method stub
		return notificationRepository.getAlerts(userId, alertId);
	}

	@Override
	public List<Integer> alertIdList(String userId, Boolean alertsCheckStatus) {
		// TODO Auto-generated method stub
		return notificationRepository.getNotificationIdListByUserId(userId, alertsCheckStatus);
	}
	
	@Override
	@Transactional
	public int readAllAlerts(String userId, List<Integer> alertIdList) {
	    return notificationRepository.readAlertsByAlertIdList(userId, alertIdList, true);
	}

}
