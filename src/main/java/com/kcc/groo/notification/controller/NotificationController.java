package com.kcc.groo.notification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.notification.data.dto.NotificationRequest;
import com.kcc.groo.notification.data.dto.NotificationUpdateRequest;
import com.kcc.groo.notification.data.model.Alerts;
import com.kcc.groo.notification.service.INotificationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/alarms")
public class NotificationController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private INotificationService notificationService;


    /**
     * @param request
     * @return
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<CommonResponse<SseEmitter>> subscribe(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        SseEmitter emitter = notificationService.subscribe(userId);
        return ResponseEntity.ok(new CommonResponse<>("connected SSE success", emitter));
    }


    /**
     * @param request
     * @return
     */
    @PostMapping("/send")
    public ResponseEntity<CommonResponse<?>> sendNotification(@RequestBody NotificationRequest request) {
        try {
            notificationService.sendNotification(request);
           return ResponseEntity.ok(new CommonResponse<>("send notification success", null));
 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new CommonResponse<>("send notification fail", null));
        }
    }

    /**
     * @param request
     * @return
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getNotifications(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        List<Alerts> alertsList = notificationService.getNotificationList(userId);
        int count = alertsList != null ? alertsList.size() : 0;

        log.info("alert list: userId={} count={}", userId, count);
        return ResponseEntity.ok(new CommonResponse<>("get notificationList", alertsList));
    }


    /**
     * @param alertId
     * @param request
     * @return
     */
    @PutMapping("/{alertId}/check")
    public ResponseEntity<CommonResponse<?>> updateCheckStatus(
            @PathVariable int alertId,
            @RequestBody(required = false) NotificationUpdateRequest notificationUpdateRequest, HttpServletRequest request) {
    	
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        if (notificationUpdateRequest == null) {
        	notificationUpdateRequest = new NotificationUpdateRequest();
        }
        notificationUpdateRequest.setAlertId(alertId);

        if (notificationUpdateRequest.getAlertsCheckStatus() == null) {
        	notificationUpdateRequest.setAlertsCheckStatus(true);
        }

        int updated = notificationService.updateAlertsCheckStatus(notificationUpdateRequest);
        if (updated <= 0) {
        	log.warn("알림 읽음 처리 실패 alertId={}", alertId);
            ResponseEntity.badRequest().body(new CommonResponse<>("cannot found this alert", null));
            
        }
        
        log.info("알림 읽음 처리 alertId={}", alertId);
        Alerts alert = notificationService.getNotificationById(userId, alertId);
        return ResponseEntity.ok(new CommonResponse<>("alert status updated success", alert));
    }
    

    /**
     * @param request
     * @return
     */
    @GetMapping("/unread-count")
    public ResponseEntity<CommonResponse<?>> getUnreadNotificationCount(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        int unreadCount = notificationService.getUnreadNotificationCount(userId);
        log.info("읽지 않은 알림 개수 조회 userId={} count={}", userId, unreadCount);

        return ResponseEntity.ok(new CommonResponse<>("count unread alerts", unreadCount));
    }

}
