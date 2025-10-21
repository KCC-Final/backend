package com.kcc.groo.notification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.notification.data.dto.NotificationRequest;
import com.kcc.groo.notification.data.dto.NotificationUpdateRequest;
import com.kcc.groo.notification.data.model.Alerts;
import com.kcc.groo.notification.service.INotificationService;

@RestController
@RequestMapping("/api/v1/alarms")
public class NotificationController {
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	@Autowired
	INotificationService notificationService;
	
	 // 실시간 구독
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam String userId) {
        return notificationService.subscribe(userId);
    }

    // 알림 생성 및 실시간 전송
    @PostMapping("/send")
    public void sendNotification(@RequestBody NotificationRequest req) {
    	
        notificationService.insertNotification(null, req);
    }

    // 알림 읽음 처리
    @PutMapping("/check")
    public void updateCheckStatus(@RequestBody NotificationUpdateRequest req) {
        notificationService.updateAlertsCheckStatus(null, req);
    }

    // 알림 목록 조회
    @GetMapping("/list")
    public List<Alerts> getList(@RequestParam String userId) {
        return notificationService.getNotificationList(userId);
    }

}
