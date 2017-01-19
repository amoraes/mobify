package com.amoraesdev.mobify.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class allows user to obtain a ticket to open a websocket connection
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@RestController
public class WebSocketTicketController {

	@Autowired
	private WebSocketTicketManager ticketManager;
	
	@RequestMapping(path = "/ws/ticket", method = RequestMethod.GET)
	public String getNewTicket(Principal user){
		return ticketManager.createTicket(user.getName());		
	}
	
}