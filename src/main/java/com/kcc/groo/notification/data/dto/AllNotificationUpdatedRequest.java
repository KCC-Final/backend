package com.kcc.groo.notification.data.dto;

import java.util.List;

import lombok.Data;

/**
 * @author kys
 * @created 2025-10-22
 */
@Data
public class AllNotificationUpdatedRequest {
	
	private List<Integer> alertIdList;

}
