package com.amoraesdev.mobify.entities;

import java.io.Serializable;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Represents a connected mobile device from a {@link User}
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Table("mobile_device")
public class MobileDevice implements Serializable{
	
	private static final long serialVersionUID = -9095978360900074267L;

	public static enum Type {
		ANDROID,
		IOS
	}
	
	@PrimaryKeyColumn(name="username",ordinal = 1,type = PrimaryKeyType.PARTITIONED)
	private String username;
	
	@PrimaryKeyColumn(name="device_id",ordinal = 1,type = PrimaryKeyType.CLUSTERED)
	private String deviceId;
	
	private String type;
	
	public MobileDevice(){
		
	}

	public MobileDevice(String username, String deviceId, Type type) {
		super();
		this.username = username;
		this.deviceId = deviceId;
		this.type = type != null ? type.name() : null;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
	public Type getType() {
		return type != null ? Type.valueOf(type) : null;
	}

	public void setType(Type type) {
		this.type = type != null ? type.name() : null;
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
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		MobileDevice other = (MobileDevice) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
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
		return "MobileDevice [username=" + username + ", deviceId=" + deviceId + ", type=" + type + "]";
	}
	
}
