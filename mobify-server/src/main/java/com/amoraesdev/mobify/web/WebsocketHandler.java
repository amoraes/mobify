package com.amoraesdev.mobify.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.amoraesdev.mobify.entities.Notification;
import com.amoraesdev.mobify.exceptions.WebSocketNotConnectedException;

/**
 * This class allows websocket topic subscription to receive new notifications
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Component
public class WebsocketHandler extends TextWebSocketHandler {
	
	private final Logger log = LoggerFactory.getLogger(WebsocketHandler.class);
	
	/**
	 * Store the active sessions from users connected
	 */
	private Map<String, WebSocketSession> activeSessions = new HashMap<>();
	
	@Autowired
	private WebSocketTicketManager ticketManager;
	
	@Override
    public void afterConnectionEstablished(WebSocketSession session) {
		//get the ticket
        String ticket = session.getUri().getQuery().replace("ticket=", "");
        WebsocketTicket wsTicket = ticketManager.getAndRemove(ticket); 
		if(wsTicket != null && !wsTicket.isExpired()){
			activeSessions.put(wsTicket.getUsername(), session);
			log.info(String.format("User '%s' successfully subscribed to receive new notifications.", wsTicket.getUsername()));
		}else{
			throw new RuntimeException("Invalid or expired ticket sent.");
		}
    }

	/**
	 * Send a notification through the websocket connection
	 * @param notification
	 * @throws WebSocketNotConnectedException
	 */
	public void sendNotification(Notification notification) throws WebSocketNotConnectedException{
		WebSocketSession session = activeSessions.get(notification.getUsername());
		if(session == null){
			throw new WebSocketNotConnectedException();
		}else{
			try {
				session.sendMessage(
						new TextMessage(
								"{\"notificationId\": \"" + notification.getId() + "\"," +
								"\"applicationId\": \"" + notification.getApplicationId() + "\"}"
							));
			} catch (IOException e) {
				throw new RuntimeException("Cannot send websocket message", e);				
			}
		}
	}
	
}
