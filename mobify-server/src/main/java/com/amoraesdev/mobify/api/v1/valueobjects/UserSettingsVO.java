package com.amoraesdev.mobify.api.v1.valueobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Value Object to represent an User Settings
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("UserSettings")
public class UserSettingsVO {

	private String username;
	
	private ApplicationVO application;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ApplicationVO getApplication() {
		return application;
	}

	public void setApplication(ApplicationVO application) {
		this.application = application;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((application == null) ? 0 : application.hashCode());
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
		UserSettingsVO other = (UserSettingsVO) obj;
		if (application == null) {
			if (other.application != null)
				return false;
		} else if (!application.equals(other.application))
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
		return "UserSettingsVO [username=" + username + ", application=" + application + "]";
	}
	
	
}