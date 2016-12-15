package com.amoraesdev.mobify.api.v1.resources;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amoraesdev.mobify.api.v1.valueobjects.NotificationPostVO;
import com.amoraesdev.mobify.entities.Notification;
import com.amoraesdev.mobify.entities.User;
import com.amoraesdev.mobify.exceptions.InvalidEntityException;
import com.amoraesdev.mobify.utils.AuthorizationHelper;

/**
 * API endpoint to manipulate {@link Notification}
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@RequestMapping("/api/v1/notifications")
public class NotificationsResource {
	
	@Autowired
	private AuthorizationHelper authorizationHelper;
	
	/**
	 * Get this value from configuration.
	 * Clients with this role can send notifications in the name of any application
	 */
	@Value("${role.master}")
	private String ROLE_MASTER;
	
	/**
	 * Post a notification, it will be sent to one or more {@link User}
	 * @param notificationVO
	 * @param bindingResult
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void post(@RequestBody @Valid NotificationPostVO notificationVO, BindingResult bindingResult){
		if(bindingResult.hasErrors()){
			throw new InvalidEntityException(NotificationPostVO.class, bindingResult);
		}
		String senderApplicationId = authorizationHelper.getClientId();
		if(!authorizationHelper.hasRole(ROLE_MASTER) 
				&& notificationVO.getApplicationId() != null
				&& !notificationVO.getApplicationId().equals(senderApplicationId)){
			throw new IllegalArgumentException("error.invalid_application_id");
		}
	}

}
