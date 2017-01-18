package com.amoraesdev.mobify.valueobjects;

/**
 * Value Object to represent a Notification
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
public class Notification {

	private String[] usernames;
	private String type;
	private String message;
	
	public Notification(String[] usernames, String type, String message) {
		super();
		this.usernames = usernames;
		this.type = type;
		this.message = message;
	}
	public String[] getUsernames() {
		return usernames;
	}
	public void setUsernames(String... usernames) {
		this.usernames = usernames;
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

}
