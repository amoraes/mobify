package com.amoraesdev.mobify.api.v1.resources;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amoraesdev.mobify.api.v1.valueobjects.NotificationPostVO;
import com.amoraesdev.mobify.api.v1.valueobjects.NotificationVO;
import com.amoraesdev.mobify.entities.Application;
import com.amoraesdev.mobify.entities.Notification;
import com.amoraesdev.mobify.entities.User;
import com.amoraesdev.mobify.exceptions.InvalidEntityException;
import com.amoraesdev.mobify.exceptions.NotFoundException;
import com.amoraesdev.mobify.repositories.ApplicationRepository;
import com.amoraesdev.mobify.repositories.UserApplicationSettingsRepository;
import com.amoraesdev.mobify.repositories.NotificationRepository;
import com.amoraesdev.mobify.services.SenderService;
import com.amoraesdev.mobify.utils.AuthorizationHelper;
import com.amoraesdev.mobify.utils.MapIdGenerator;
import com.datastax.driver.core.utils.UUIDs;

/**
 * API endpoint to manipulate {@link Notification}
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@RestController
public class NotificationsResource {
	
	public static final String BASE_URL = "/api/v1/notifications";
	public static final String BASE_URL_WITH_APPLICATION_ID = "/api/v1/applications/{applicationId}/notifications";
	
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
	@Value("${roles.client_master}")
	private String ROLE_CLIENT_MASTER;
	
	/**
	 * Post a notification, it will be sent to one or more {@link User}
	 * @param notificationVO
	 * @param bindingResult
	 * @throws NotFoundException 
	 */
	@RequestMapping(path = BASE_URL_WITH_APPLICATION_ID, method = RequestMethod.POST)
	public void post(
			@PathVariable("applicationId") String applicationId, 
			@RequestBody @Valid NotificationPostVO notificationVO, 
			BindingResult bindingResult) throws NotFoundException{
		if(bindingResult.hasErrors()){
			throw new InvalidEntityException(NotificationPostVO.class, bindingResult);
		}
		String senderApplicationId = authorizationHelper.getClientId();
		//verifies if this client has authorization to send messages in the name of the application id
		if(!authorizationHelper.hasRole(ROLE_CLIENT_MASTER) 
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
	
	/**
	 * Get a {@link Notification} based on the primary key
	 * @param applicationId
	 * @param notificationId
	 * @param user
	 * @return
	 * @throws NotFoundException 
	 */
	@RequestMapping(path = BASE_URL_WITH_APPLICATION_ID + "/{notificationId}",method = RequestMethod.GET)
	public NotificationVO getByPrimaryKey(
			@PathVariable("applicationId") String applicationId, 
			@PathVariable("notificationId") String notificationId,
			Principal user) throws NotFoundException{
		Notification notification = notificationRepository.findOne(MapIdGenerator.createNotificationPK(notificationId, authorizationHelper.getUsername(user), applicationId));
		//not found?
		if(notification == null){
			throw new NotFoundException("error.entity_not_found", "Notification", String.format("[notificationId=%s, applicationId=%s, username=%s]", notificationId, applicationId, authorizationHelper.getUsername(user)));
		}
		//if it is the first time retrieving this notification, record the current timestamp as timestampReceived
		if(notification.getTimestampReceived() == null){
			notification.setTimestampReceived(new Date());
			notificationRepository.save(notification);
		}
		//populate the value object and return it
		NotificationVO vo = new NotificationVO();
		vo.setNotificationId(notification.getId().toString());
		vo.setUsername(notification.getUsername());
		vo.setApplicationId(notification.getApplicationId());
		vo.setType(notification.getType());
		vo.setMessage(notification.getMessage());
		vo.setTimestampRead(notification.getTimestampRead());
		vo.setTimestampReceived(notification.getTimestampReceived());
		vo.setTimestampSent(notification.getTimestampSent());
		return vo;
	}
	
	/**
	 * Get all unreceived {@link Notification} based on username
	 * @param user
	 * @return
	 * @throws NotFoundException 
	 */
	@RequestMapping(path = BASE_URL + "/unreceived", method = RequestMethod.GET)
	public Collection<NotificationVO> getUnreceived(Principal user) {
		//retrieve from the database
		Collection<Notification> notifications = notificationRepository.findByUsernameAndNotReceived(authorizationHelper.getUsername(user));
		//build a list to be returned
		Collection<NotificationVO> vos = new ArrayList<>();
		if(notifications.isEmpty()){
			return vos;
		}
		Date now = new Date();
		notifications.forEach(notification -> {
			//first time retrieving this notification, record the current timestamp as timestampReceived
			notification.setTimestampReceived(now);
			//populate the value object and add it on the return list 
			NotificationVO vo = new NotificationVO();
			vo.setNotificationId(notification.getId().toString());
			vo.setUsername(notification.getUsername());
			vo.setApplicationId(notification.getApplicationId());
			vo.setType(notification.getType());
			vo.setMessage(notification.getMessage());
			vo.setTimestampRead(notification.getTimestampRead());
			vo.setTimestampReceived(notification.getTimestampReceived());
			vo.setTimestampSent(notification.getTimestampSent());
			vos.add(vo);
		});
		//save the notifications updated with timestampReceived
		//notificationRepository.save(notifications);
		
		return vos;
	}
	
	/**
	 * Mark a {@link Notification} as read and set the timestamp
	 * @param applicationId
	 * @param notificationId
	 * @param user
	 * @return
	 * @throws NotFoundException 
	 */
	@RequestMapping(path = BASE_URL_WITH_APPLICATION_ID + "/{notificationId}/read",method = RequestMethod.PATCH)
	public void patchByPrimaryKeyAsRead(
			@PathVariable("applicationId") String applicationId, 
			@PathVariable("notificationId") String notificationId,
			Principal user) throws NotFoundException{
		Notification notification = notificationRepository.findOne(MapIdGenerator.createNotificationPK(notificationId, authorizationHelper.getUsername(user), applicationId));
		//not found?
		if(notification == null){
			throw new NotFoundException("error.entity_not_found", "Notification", 
					String.format("[notificationId=%s, applicationId=%s, username=%s]", 
							notificationId, applicationId, authorizationHelper.getUsername(user)));
		}
		//if it is the first time retrieving this notification, record the current timestamp as timestampReceived
		if(notification.getTimestampRead() == null){
			notification.setTimestampRead(new Date());
			notificationRepository.save(notification);
		}
	}
	
	

}
