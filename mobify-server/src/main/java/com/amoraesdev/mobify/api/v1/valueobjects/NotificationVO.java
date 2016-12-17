package com.amoraesdev.mobify.api.v1.valueobjects;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Value Object to represent a new Notification sent to a user
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@JsonTypeName("Notification")
public class NotificationVO implements Serializable {

	private static final long serialVersionUID = -6902791979734017283L;
	
	private UUID id;
	
	private String applicationId;
	
	@NotNull
	private String type;
	
	@NotNull
	private String message;
	
	@NotNull
	private Date timestampSent;
	
	private Date timestampReceived;

	private Date timestampRead;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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
	
}
