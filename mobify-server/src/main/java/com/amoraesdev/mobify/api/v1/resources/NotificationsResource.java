package com.amoraesdev.mobify.api.v1.resources;

import java.security.Principal;
import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.repository.MapId;
import org.springframework.data.cassandra.repository.support.BasicMapId;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amoraesdev.mobify.api.v1.valueobjects.NotificationPostVO;
import com.amoraesdev.mobify.api.v1.valueobjects.NotificationVO;
import com.amoraesdev.mobify.entities.Application;
import com.amoraesdev.mobify.entities.Notification;
import com.amoraesdev.mobify.entities.User;
import com.amoraesdev.mobify.exceptions.InvalidEntityException;
import com.amoraesdev.mobify.exceptions.NotFoundException;
import com.amoraesdev.mobify.repositories.ApplicationRepository;
import com.amoraesdev.mobify.repositories.NotificationRepository;
import com.amoraesdev.mobify.services.SenderService;
import com.amoraesdev.mobify.utils.AuthorizationHelper;
import com.amoraesdev.mobify.utils.MapIdGenerator;
import com.datastax.driver.core.utils.UUIDs;

/**
 * API endpoint to manipulate {@link Notification}
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@RequestMapping(NotificationsResource.BASE_URL)
public class NotificationsResource {
	
	public static final String BASE_URL = "/api/v1/applications/{applicationId}/notifications";
	
	@Autowired
	private AuthorizationHelper authorizationHelper;
	
	@Autowired
	private ApplicationRepository applicationRepositoy;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private SenderService senderService;
	
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
	 * @throws NotFoundException 
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void post(
			@PathVariable("applicationId") String applicationId, 
			@RequestBody @Valid NotificationPostVO notificationVO, 
			BindingResult bindingResult) throws NotFoundException{
		if(bindingResult.hasErrors()){
			throw new InvalidEntityException(NotificationPostVO.class, bindingResult);
		}
		String senderApplicationId = authorizationHelper.getClientId();
		//verifies if this client has authorization to send messages in the name of the application id
		if(!authorizationHelper.hasRole(ROLE_MASTER) 
				&& applicationId != null
				&& !applicationId.equals(senderApplicationId)){
			throw new IllegalArgumentException("error.invalid_application_id");
		}
		//verifies if the application id exists, a new application needs to be configured in the admin control panel first
		Application application = applicationRepositoy.findByApplicationId(senderApplicationId);
		if(application == null){
			throw new IllegalArgumentException("error.application_not_configured");
		}
		Date now = new Date();
		//everything is fine, save a notification for each user
		for(String username : notificationVO.getUsernames()){
			Notification notification = new Notification(UUIDs.timeBased(), username, senderApplicationId, now, notificationVO.getType(), notificationVO.getMessage());
			notificationRepository.save(notification);
			senderService.sendNotification(notification);
		}
	}
	
	@RequestMapping(path = "/{notificationId}",method = RequestMethod.GET)
	public NotificationVO getByPrimaryKey(
			@PathVariable("applicationId") String applicationId, 
			@PathVariable("notificationId") String notificationId,
			Principal user){
		Notification notification = notificationRepository.findOne(MapIdGenerator.createNotificationKey(notificationId, user.getName(), applicationId));
		NotificationVO notificationVO = new NotificationVO();
		//TODO implement the translation an further unit testing
		return notificationVO;
	}

}
