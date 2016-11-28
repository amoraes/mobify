package com.amoraesdev.mobify.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Represents a notification message sent to one {@link User}
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Table
public class Notification implements Serializable {

	private static final long serialVersionUID = -7930874841951049035L;

	@PrimaryKeyColumn(name = "id", ordinal = 3,type = PrimaryKeyType.CLUSTERED)
	private UUID id;
	
	/**
	 * The {@link User} who will be notified
	 */
	@PrimaryKeyColumn(name="username",ordinal = 0,type = PrimaryKeyType.PARTITIONED)
	private String username;
	
	/**
	 * This is the {@link Application} which originated the notification message
	 */
	@PrimaryKeyColumn(name="application_id",ordinal = 1,type = PrimaryKeyType.CLUSTERED)
	private String applicationId;
	
	@PrimaryKeyColumn(name = "timestamp_sent",ordinal = 2, 
			ordering = Ordering.DESCENDING, type = PrimaryKeyType.CLUSTERED)
	private Date timestampSent;
	
	@Column("timestamp_received")
	private Date timestampReceived;

	@Column("timestamp_read")
	private Date timestampRead;
	
	private String type;
	
	private String message;

	/**
	 * Creates a new {@link Notification} to be sent to a {@link User} 
	 * @param id
	 * @param username 
	 * @param applicationId
	 * @param timestampSent
	 * @param type
	 * @param message
	 */
	public Notification(UUID id, String username, String applicationId, Date timestampSent, String type, String message) {
		super();
		this.id = id;
		this.username = username;
		this.applicationId = applicationId;
		this.timestampSent = timestampSent;
		this.type = type;
		this.message = message;
	}

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
	
}
