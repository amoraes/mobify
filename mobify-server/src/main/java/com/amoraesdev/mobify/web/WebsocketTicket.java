package com.amoraesdev.mobify.web;

import java.util.Date;

/**
 * This class represents a websocket connection ticket
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
public class WebsocketTicket {
	private long expirationTimestamp;
	private String username;

	/**
	 * Creates a new websocket ticket due to expire in 'n' seconds 
	 * @param username
	 * @param expiresIn
	 */
	public WebsocketTicket(String username, int expiresIn){
		this.username = username;
		this.expirationTimestamp = new Date().getTime() + (expiresIn * 1000);
	}
	
	public boolean isExpired(){
		if(new Date().getTime() > this.expirationTimestamp){
			return true;
		}else{
			return false;
		}
	}
	public String getUsername() {
		return username;
	}
	
}