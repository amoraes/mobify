package com.amoraesdev.mobify.web;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * This class manages websocket connection tickets
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Component
public class WebSocketTicketManager {
	
	@Value("${websocket.ticket-expiration}")
	private int ticketExpirationInSeconds;
	
	private SecureRandom random = new SecureRandom();
	
	private Map<String, WebsocketTicket> mapTicketUsername = new TreeMap<>();
	
	/**
	 * Create a new ticket valid for some seconds
	 * @param username
	 * @return
	 */
	public String createTicket(String username){
		String ticketIdentification = new BigInteger(130, random).toString(32);
		mapTicketUsername.put(ticketIdentification, new WebsocketTicket(username, ticketExpirationInSeconds));
		return ticketIdentification;
	}
	
	/**
	 * Return a ticket and invalidate it removing from the queue
	 * @param ticket
	 * @return
	 */
	public WebsocketTicket getAndRemove(String ticket){
		WebsocketTicket wsTicket = mapTicketUsername.get(ticket);
		mapTicketUsername.remove(ticket);
		return wsTicket;
	}
	
}