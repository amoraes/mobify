package com.amoraesdev.mobify.entities;

import java.io.Serializable;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Represents {@link Application} settings for an {@link User}
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Table("user_application_settings")
public class UserApplicationSettings implements Serializable{
	
	private static final long serialVersionUID = 4471808282302862623L;

	@PrimaryKeyColumn(name="username",ordinal = 1,type = PrimaryKeyType.PARTITIONED)
	private String username;
	
	@PrimaryKeyColumn(name="application_id",ordinal = 1,type = PrimaryKeyType.CLUSTERED)
	private String applicationId;
	
	private Boolean silent;
	
	public UserApplicationSettings(){
		
	}

	public UserApplicationSettings(String username, String applicationId, Boolean silent) {
		super();
		this.username = username;
		this.applicationId = applicationId;
		this.silent = silent;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		UserApplicationSettings other = (UserApplicationSettings) obj;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ApplicationUserSettings [username=" + username + ", applicationId=" + applicationId + "]";
	}

	public Boolean getSilent() {
		return silent;
	}

	public void setSilent(Boolean silent) {
		this.silent = silent;
	}
	
}
