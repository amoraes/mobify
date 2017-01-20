package com.amoraesdev.mobify.api.valueobjects;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Value Object to represent a Notification sent to a user (return values only)
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("Notification")
public class NotificationVO {

	private String notificationId;
	
	private String applicationId;
	
	private String type;
	
	private String message;
	
	private String username;
	
	private Date timestampSent;
	
	private Date timestampReceived;

	private Date timestampRead;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getTimestampSent() {
		return timestampSent;
	}

	public void setTimestampSent(Date timestampSent) {
		this.timestampSent = timestampSent;
	}

	public Date getTimestampReceived() {
		return timestampReceived;
	}

	public void setTimestampReceived(Date timestampReceived) {
		this.timestampReceived = timestampReceived;
	}

	public Date getTimestampRead() {
		return timestampRead;
	}

	public void setTimestampRead(Date timestampRead) {
		this.timestampRead = timestampRead;
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
		NotificationVO other = (NotificationVO) obj;
		if (notificationId == null) {
			if (other.notificationId != null)
				return false;
		} else if (!notificationId.equals(other.notificationId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NotificationVO [notificationId=" + notificationId + ", username=" + username + ", applicationId=" + applicationId + ", type="
				+ type + ", message=" + message + "]";
	}
	
}
