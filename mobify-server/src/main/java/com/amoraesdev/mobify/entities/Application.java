package com.amoraesdev.mobify.entities;

import java.io.Serializable;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Represents an application capable of sending {@link Notification} to some {@link User}
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Table
public class Application implements Serializable {
	
	private static final long serialVersionUID = 5940047209039790892L;
	
	/**
	 * This one should be the same as your Client Id in the OAuth authentication server
	 */
	@PrimaryKeyColumn(name="application_id",ordinal = 0,type = PrimaryKeyType.PARTITIONED)
	private String applicationId;
	
	/**
	 * This is the name users will see on their mobile devices
	 */
	private String name;
	
	/**
	 * This is the icon users will see on their mobile devices
	 */
	private String icon;
	
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
}
