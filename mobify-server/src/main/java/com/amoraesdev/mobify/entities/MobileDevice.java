package com.amoraesdev.mobify.entities;

import java.io.Serializable;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
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
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
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
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		return true;
	}
	
}
