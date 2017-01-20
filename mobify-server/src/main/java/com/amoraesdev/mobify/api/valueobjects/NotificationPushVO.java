package com.amoraesdev.mobify.api.valueobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Value Object to represent a new Notification sent to a user (without content)
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("Notification")
public class NotificationPushVO {

	private String notificationId;
	
	private String applicationId;
	
	private String username;
	
	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((notificationId == null) ? 0 : notificationId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NotificationPushVO other = (NotificationPushVO) obj;
		if (notificationId == null) {
			if (other.notificationId != null)
				return false;
		} else if (!notificationId.equals(other.notificationId))
			return false;
		return true;
	}

}
