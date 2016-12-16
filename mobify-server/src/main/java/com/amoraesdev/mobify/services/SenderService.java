package com.amoraesdev.mobify.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amoraesdev.mobify.entities.Notification;
import com.amoraesdev.mobify.utils.RestErrorsControllerAdvice;

/**
 * This class is responsible for sending notifications to the mobile devices
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Component
public class SenderService {
	private final Logger log = LoggerFactory.getLogger(RestErrorsControllerAdvice.class);
	
	/**
	 * Send a notification in background
	 * Async processing (fire and forget)
	 * @param notification
	 */
	public void sendNotification(Notification notification){
		//TODO implement the logic to send to the mobile devices connected
		log.debug("Sending notification".concat(notification.toString()));
	}
}
