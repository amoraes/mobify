package com.amoraesdev.mobify.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.cassandra.core.Ordering;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.Indexed;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Represents a notification message sent to one {@link User}
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Table
public class Notification implements Serializable {

	private static final long serialVersionUID = -7930874841951049035L;

	@PrimaryKeyColumn(name = "id", ordinal = 2, ordering = Ordering.DESCENDING, type = PrimaryKeyType.CLUSTERED)
	private UUID id;
	
	/**
	 * The {@link User} who will be notified
	 */
	@PrimaryKeyColumn(name="username", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String username;
	
	/**
	 * This is the {@link Application} which originated the notification message
	 */
	@PrimaryKeyColumn(name="application_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
	private String applicationId;
	
	@Column("timestamp_sent")
	private Date timestampSent;
	
	@Indexed("notification_received")
	private Boolean received;
	
	@Column("timestamp_received")
	private Date timestampReceived;

	private Boolean read;
	
	@Column("timestamp_read")
	private Date timestampRead;
	
	private String type;
	
	private String message;

	/**
	 * Creates a {@link Notification} to be sent to a {@link User}
	 * @param id
	 * @param username
	 * @param applicationId
	 * @param timestampSent
	 * @param received
	 * @param timestampReceived
	 * @param read
	 * @param timestampRead
	 * @param type
	 * @param message
	 */
	public Notification(UUID id, String username, String applicationId, Date timestampSent, Boolean received,
			Date timestampReceived, Boolean read, Date timestampRead, String type, String message) {
		super();
		this.id = id;
		this.username = username;
		this.applicationId = applicationId;
		this.timestampSent = timestampSent;
		this.received = received;
		this.timestampReceived = timestampReceived;
		this.read = read;
		this.timestampRead = timestampRead;
		this.type = type;
		this.message = message;
	}
	
	/**
	 * Creates a new (non-received and non-read) {@link Notification} to be sent to a {@link User}
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
		this.received = false;
		this.read = false;
		this.type = type;
		this.message = message;
	}
	
	public Notification(){
		
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
		if(timestampReceived != null) 
			this.received = true; //also mark received as true
		this.timestampReceived = timestampReceived;
	}

	public Date getTimestampRead() {
		return timestampRead;
	}

	public void setTimestampRead(Date timestampRead) {
		if(timestampRead != null)
			this.read = true; //also mark read as true
		this.timestampRead = timestampRead;
	}

	public Boolean getReceived() {
		return received;
	}

	public void setReceived(Boolean received) {
		this.received = received;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Notification other = (Notification) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Notification [id=" + id + ", username=" + username + ", applicationId=" + applicationId + ", type="
				+ type + ", message=" + message + "]";
	}
	
}
