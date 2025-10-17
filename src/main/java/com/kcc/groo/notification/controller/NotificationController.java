package com.kcc.groo.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kcc.groo.jwt.JwtTokenProvider;
import com.kcc.groo.notification.service.INotificationService;

@RestController
@RequestMapping("/api/v1/alarms")
public class NotificationController {
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	@Autowired
	INotificationService notificationService;

}
