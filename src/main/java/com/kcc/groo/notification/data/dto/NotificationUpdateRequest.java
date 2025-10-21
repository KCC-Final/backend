package com.kcc.groo.notification.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationUpdateRequest {

	private int alertId;
	private Boolean alertsCheckStatus;
	
}
