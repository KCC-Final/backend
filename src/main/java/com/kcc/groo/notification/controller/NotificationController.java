package com.kcc.groo.notification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.kcc.groo.common.dto.CommonResponse;
import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.notification.data.dto.AllNotificationUpdatedRequest;
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
     * @author kys
     * @author uyh
     * @created 2025-10-21
     * @updated 2025-10-30
     * SSE 구독
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(HttpServletRequest request) {
        // 쿠키 기반 액세스 토큰 추출
        String accessToken = jwtTokenProvider.resolveAccessTokenFromCookie(request);

        // 토큰이 없으면 예외 처리
        if (accessToken == null || !jwtTokenProvider.validateToken(accessToken)) {
            throw new RuntimeException("Invalid or missing token");
        }

        String userId = jwtTokenProvider.getUserId(accessToken);
        log.info(" SSE 구독 요청 userId={}", userId);

        return notificationService.subscribe(userId);
    }



    /**
     * @param request
     * @return
     * @author kys
     * @created 2025-10-22
     * 실시간 알림 전송
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
     * @author kys
     * @created 2025-10-22
     * 알림 정보 조회
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
     * @author kys
     * @created 2025-10-22
     * 알림 읽음처리 (단건)
     */
    @PutMapping("{alertId}/check")
    public ResponseEntity<CommonResponse<?>> updateCheckStatus(
    		@PathVariable("alertId") int alertId,
            @RequestBody NotificationUpdateRequest notificationUpdateRequest, HttpServletRequest request) {
    	
    	//getId
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);
        
        int updated = notificationService.updateAlertsCheckStatus(userId, alertId, notificationUpdateRequest);
        
        if (updated > 0) {
        	log.info("알림 읽음 처리 완료: alertId={}, userId={}", alertId, userId);
        } else {
        	log.warn("알림 읽음 처리 실패: alertId={}, userId={}", alertId, userId);
        }
       
        Alerts alert = notificationService.getNotificationById(userId, alertId);
        return ResponseEntity.ok(new CommonResponse<>("alert status updated success", alert));
    }
    

    /**
     * @param request
     * @return
     * @author kys
     * @created 2025-10-22
     * 읽지 않은 알림 수
     */
    @GetMapping("/unread-count")
    public ResponseEntity<CommonResponse<?>> getUnreadNotificationCount(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);

        int unreadCount = notificationService.getUnreadNotificationCount(userId);
        log.info("읽지 않은 알림 개수 조회 userId={} count={}", userId, unreadCount);

        return ResponseEntity.ok(new CommonResponse<>("count unread alerts", unreadCount));
    }
    
    /**
     * @param userId
     * @return
     * @author kys
     * @created 2025-10-22
     * 특정 유저의 알림 전체 목록 조회
     */
    @GetMapping("/list")
    public ResponseEntity<CommonResponse<?>> getAlertList(HttpServletRequest request) {
    	String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String userId = jwtTokenProvider.getUserId(accessToken);
        List<Alerts> alertsList = notificationService.getNotificationList(userId);
        return ResponseEntity.ok(new CommonResponse<>("User alert list", alertsList));
    }
    
    /**
     * @param alertId
     * @param request
     * @return
     * @author kys
     * @created 2025-10-22
     * 알림 전체 읽음처리
     */
    @PutMapping("/check-list")
	public ResponseEntity<CommonResponse<?>> updatedAllAlertsStatus(@RequestBody AllNotificationUpdatedRequest notificationUpdateRequest, HttpServletRequest request) {

		// get userId
		String accessToken = jwtTokenProvider.resolveAccessToken(request);
		String userId = jwtTokenProvider.getUserId(accessToken);
		
	    List<Integer> alertIdList = notificationUpdateRequest.getAlertIdList();

	    if (alertIdList == null || alertIdList.isEmpty()) {
	    	alertIdList = notificationService.alertIdList(userId, false);
	    }
	    if (alertIdList == null || alertIdList.isEmpty()) {
	    	return ResponseEntity.badRequest()
	    			.body(new CommonResponse<>("Please select at least one alert to updated status", null));
	    }

	    int updatedCount = notificationService.readAllAlerts(userId, alertIdList);

	    return ResponseEntity.ok(
	        new CommonResponse<>("The selected alerts have been updated successfully", updatedCount)
	    );
	}

}
