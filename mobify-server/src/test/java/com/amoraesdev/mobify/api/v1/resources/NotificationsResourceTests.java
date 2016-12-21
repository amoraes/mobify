package com.amoraesdev.mobify.api.v1.resources;

import static com.amoraesdev.mobify.TestHelper.convertObjectToJsonBytes;
import static org.mockito.Mockito.times;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.cassandra.repository.MapId;
import org.springframework.test.web.servlet.MockMvc;

import com.amoraesdev.mobify.Constants;
import com.amoraesdev.mobify.TestHelper;
import com.amoraesdev.mobify.api.v1.valueobjects.NotificationPostVO;
import com.amoraesdev.mobify.entities.Application;
import com.amoraesdev.mobify.entities.Notification;
import com.amoraesdev.mobify.repositories.ApplicationRepository;
import com.amoraesdev.mobify.repositories.NotificationRepository;
import com.amoraesdev.mobify.services.SenderService;
import com.amoraesdev.mobify.utils.AuthorizationHelper;
import com.amoraesdev.mobify.utils.RestErrorsControllerAdvice;
import com.datastax.driver.core.utils.UUIDs;

/**
 * {@link NotificationsResource} unit tests 
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
public class NotificationsResourceTests {

	private MockMvc mockMvc;
	
	private final String BASE_URL_WITH_APPLICATION_ID = NotificationsResource.BASE_URL_WITH_APPLICATION_ID;
	private final String BASE_URL = NotificationsResource.BASE_URL;
	
	@Mock
	private AuthorizationHelper authorizationHelper;
	
	@Mock
	private ApplicationRepository applicationRepository;
	
	@Mock
	private NotificationRepository notificationRepository;
	
	@Mock
	private SenderService senderService;
	
	@InjectMocks
	private NotificationsResource resource;
	
	/**
	 * Get this value from configuration.
	 * Clients with this role can send notifications in the name of any application
	 */
	@Value("${role.master}")
	private String ROLE_MASTER;
	
	/**
	 * Initial configuration
	 */
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		RestErrorsControllerAdvice advice = new RestErrorsControllerAdvice();
		advice.setMessageSource(TestHelper.getDefaultMessageSource());
		this.mockMvc = standaloneSetup(resource)
				.setControllerAdvice(advice)
			.build();
	}
	
	/**
	 * post as a regular user: 
	 * verify if the notification is correctly inserted in the database and sent to the users
	 */	
	@Test
	public void testPostAsRegularUser() throws Exception{
		NotificationPostVO postVO = new NotificationPostVO();
		postVO.setMessage("This is a test message");
		postVO.setType("Alert");
		postVO.setUsernames("amoraes", "sanfatec");
		
		Application application = new Application();
		application.setApplicationId("my-client-id");
		application.setName("My App");
		
		when(authorizationHelper.hasRole(ROLE_MASTER)).thenReturn(false);
		when(authorizationHelper.getClientId()).thenReturn("my-client-id");
		when(applicationRepository.findByApplicationId("my-client-id")).thenReturn(application);
		
		this.mockMvc
			.perform(post(BASE_URL_WITH_APPLICATION_ID, "my-client-id")
					.contentType(Constants.APPLICATION_JSON_UTF8)
					.content(convertObjectToJsonBytes(postVO))	
					)
				.andExpect(status().isOk());
		//verify if saved and sent two notifications, one for each username
		verify(notificationRepository, times(2)).save(Matchers.<Notification>any());
		verify(senderService, times(2)).sendNotification(Matchers.<Notification>any());
	}
	
	
	/**
	 * post as a master user (master users can send notifications in the behalf of any other): 
	 * verify if the notification is correctly inserted in the database and sent to the user
	 */	
	@Test
	public void testPostAsMasterUser() throws Exception{
		NotificationPostVO postVO = new NotificationPostVO();
		postVO.setMessage("This is a test message");
		postVO.setType("Alert");
		postVO.setUsernames("amoraes");
		
		Application application = new Application();
		application.setApplicationId("my-client-id");
		application.setName("My App");
		
		when(authorizationHelper.hasRole(ROLE_MASTER)).thenReturn(true);
		when(authorizationHelper.getClientId()).thenReturn("my-client-id");
		when(applicationRepository.findByApplicationId("my-client-id")).thenReturn(application);
		
		this.mockMvc
			.perform(post(BASE_URL_WITH_APPLICATION_ID, "other-client-id") //sets a different application (master clients are allowed to send notifications in other apps behalf)
					.contentType(Constants.APPLICATION_JSON_UTF8)
					.content(convertObjectToJsonBytes(postVO))	
					)
				.andExpect(status().isOk());
		//verify if saved and sent two notifications, one for each username
		verify(notificationRepository, times(1)).save(Matchers.<Notification>any());
		verify(senderService, times(1)).sendNotification(Matchers.<Notification>any());
	}
	
	/**
	 * post as regular user but the application is not configured yet
	 */	
	@Test
	public void testPostAsRegularUserApplicationNotConfiguredYet() throws Exception{
		NotificationPostVO postVO = new NotificationPostVO();
		postVO.setMessage("This is a test message");
		postVO.setType("Alert");
		postVO.setUsernames("amoraes", "sanfatec");
			
		when(authorizationHelper.hasRole(ROLE_MASTER)).thenReturn(false);
		when(authorizationHelper.getClientId()).thenReturn("my-client-id");
		when(applicationRepository.findByApplicationId("my-client-id")).thenReturn(null);
		
		this.mockMvc
			.perform(post(BASE_URL_WITH_APPLICATION_ID, "my-client-id")
					.contentType(Constants.APPLICATION_JSON_UTF8)
					.content(convertObjectToJsonBytes(postVO))	
					)
				.andExpect(status().isBadRequest());
		
		//verify if never saved or sent any notifications
		verify(notificationRepository, never()).save(Matchers.<Notification>any());
		verify(senderService, never()).sendNotification(Matchers.<Notification>any());	
	}
	
	/**
	 * post as regular user with invalid data: 
	 * verify if the api returns the correct error code (422)
	 */	
	@Test
	public void testPostAsRegularUserWithInvalidFields() throws Exception{
		NotificationPostVO postVO = new NotificationPostVO();
		postVO.setMessage(null); //message is a required field
		postVO.setType("Alert");
		postVO.setUsernames("amoraes");
			
		when(authorizationHelper.hasRole(ROLE_MASTER)).thenReturn(false);
		when(authorizationHelper.getClientId()).thenReturn("my-client-id");
		this.mockMvc
			.perform(post(BASE_URL_WITH_APPLICATION_ID, "my-client-id")
					.contentType(Constants.APPLICATION_JSON_UTF8)
					.content(convertObjectToJsonBytes(postVO))	
					)
				.andExpect(status().isUnprocessableEntity());
		//verify if never saved or sent any notifications
		verify(notificationRepository, never()).save(Matchers.<Notification>any());
		verify(senderService, never()).sendNotification(Matchers.<Notification>any());	
	}
	
	/**
	 * post as regular user with invalid application id: 
	 * verify if the api returns the correct error code (400)
	 */	
	@Test
	public void testPostAsRegularUserWithInvalidId() throws Exception{
		NotificationPostVO postVO = new NotificationPostVO();
		postVO.setMessage("This is a special notification message wrote for you"); 
		postVO.setType("Important");
		postVO.setUsernames("amoraes");
			
		when(authorizationHelper.hasRole(ROLE_MASTER)).thenReturn(false);
		when(authorizationHelper.getClientId()).thenReturn("my-client-id");
		this.mockMvc
			.perform(post(BASE_URL_WITH_APPLICATION_ID, "other-client-id")
					.contentType(Constants.APPLICATION_JSON_UTF8)
					.content(convertObjectToJsonBytes(postVO))	
					)
				.andExpect(status().isBadRequest());
		//verify if never saved or sent any notifications
		verify(notificationRepository, never()).save(Matchers.<Notification>any());
		verify(senderService, never()).sendNotification(Matchers.<Notification>any());	
	}
	
	/**
	 * get the notification by its primary key
	 * as it is the first time retrieving this message, the controller should record the timestampReceived field
	 * @throws Exception
	 */
	@Test
	public void testGetByPrimaryKeyFirstTime() throws Exception {
		String applicationId = "my-client-id";
		UUID notificationId = UUIDs.timeBased();
		String username = "alessandro";
		Date timestampSent = new Date();
		//notification without timestampReceived (first time)
		Notification notification = new Notification(notificationId, username, applicationId, timestampSent, "Alert", "Message content");
		when(notificationRepository.findOne(Matchers.<MapId>any())).thenReturn(notification);
		when(authorizationHelper.getUsername(Matchers.any())).thenReturn(username);
		
		this.mockMvc
		.perform(get(BASE_URL_WITH_APPLICATION_ID+"/{notificationId}", applicationId, notificationId.toString())
				.accept(Constants.APPLICATION_JSON_UTF8)
				)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.notificationId", equalTo(notificationId.toString())))
			.andExpect(jsonPath("$.username", equalTo(username)))
			.andExpect(jsonPath("$.applicationId", equalTo(applicationId)))
			.andExpect(jsonPath("$.type", equalTo("Alert")))
			.andExpect(jsonPath("$.message", equalTo("Message content")))
			.andExpect(jsonPath("$.timestampSent", equalTo(timestampSent.getTime())))
			.andExpect(jsonPath("$.timestampReceived", notNullValue()))
			.andExpect(jsonPath("$.timestampRead", nullValue()));
		
		//verify if the method called save to update the timestamp received
		verify(notificationRepository, times(1)).save(Matchers.<Notification>any());
		
	}
	
	/**
	 * get the notification by its primary key (not first time)
	 * @throws Exception
	 */
	@Test
	public void testGetByPrimaryKey() throws Exception {
		String applicationId = "my-client-id";
		UUID notificationId = UUIDs.timeBased();
		String username = "alessandro";
		Date timestampSent = new Date();
		Date timestampReceived = new Date();
		Notification notification = new Notification(notificationId, username, applicationId, timestampSent, true, timestampReceived, false, null, "Alert", "Message content");
		when(notificationRepository.findOne(Matchers.<MapId>any())).thenReturn(notification);
		when(authorizationHelper.getUsername(Matchers.any())).thenReturn(username);
		
		this.mockMvc
		.perform(get(BASE_URL_WITH_APPLICATION_ID+"/{notificationId}", applicationId, notificationId.toString())
				.accept(Constants.APPLICATION_JSON_UTF8)
				)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.notificationId", equalTo(notificationId.toString())))
			.andExpect(jsonPath("$.username", equalTo(username)))
			.andExpect(jsonPath("$.applicationId", equalTo(applicationId)))
			.andExpect(jsonPath("$.type", equalTo("Alert")))
			.andExpect(jsonPath("$.message", equalTo("Message content")))
			.andExpect(jsonPath("$.timestampSent", equalTo(timestampSent.getTime())))
			.andExpect(jsonPath("$.timestampReceived", equalTo(timestampReceived.getTime())))
			.andExpect(jsonPath("$.timestampRead", nullValue()));
		
		//verify if the method called save to update the timestamp received
		verify(notificationRepository, never()).save(Matchers.<Notification>any());
		
	}
	
	/**
	 * get the notification by its primary key (not first time)
	 * @throws Exception
	 */
	@Test
	public void testGetByPrimaryKeyNotFound() throws Exception {
		String applicationId = "my-client-id";
		UUID notificationId = UUIDs.timeBased();
		String username = "alessandro";
		when(notificationRepository.findOne(Matchers.<MapId>any())).thenReturn(null);
		when(authorizationHelper.getUsername(Matchers.any())).thenReturn(username);
		
		this.mockMvc
		.perform(get(BASE_URL_WITH_APPLICATION_ID+"/{notificationId}", applicationId, notificationId.toString())
				.accept(Constants.APPLICATION_JSON_UTF8)
				)
			.andExpect(status().isNotFound());
		
	}
	
	/**
	 * get all the unreceived notifications of an user
	 * as it is the first time retrieving this message, the controller should record the timestampReceived field
	 * @throws Exception
	 */
	@Test
	public void testGetUnreceived() throws Exception {
		String applicationId = "my-client-id";
		String username = "alessandro";
		Date timestampSent = new Date();
		UUID notificationId1 = UUIDs.timeBased();
		UUID notificationId2 = UUIDs.timeBased();
		//notification without timestampReceived (first time)
		Notification notification1 = new Notification(notificationId1, username, applicationId, timestampSent, "Alert", "Message content");
		Notification notification2 = new Notification(notificationId2, username, applicationId, timestampSent, "Important", "Don't forget to wash your car today");
		
		when(notificationRepository.findByUsernameAndNotReceived(username)).thenReturn(Arrays.asList(notification1, notification2));
		when(authorizationHelper.getUsername(Matchers.any())).thenReturn(username);
		
		this.mockMvc
		.perform(get(BASE_URL+"/unreceived")
				.accept(Constants.APPLICATION_JSON_UTF8)
				)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0].notificationId", equalTo(notificationId1.toString())))
			.andExpect(jsonPath("$[0].username", equalTo(username)))
			.andExpect(jsonPath("$[0].applicationId", equalTo(applicationId)))
			.andExpect(jsonPath("$[0].type", equalTo("Alert")))
			.andExpect(jsonPath("$[0].message", equalTo("Message content")))
			.andExpect(jsonPath("$[0].timestampSent", equalTo(timestampSent.getTime())))
			.andExpect(jsonPath("$[0].timestampReceived", notNullValue()))
			.andExpect(jsonPath("$[0].timestampRead", nullValue()))
			.andExpect(jsonPath("$[1].notificationId", equalTo(notificationId2.toString())))
			.andExpect(jsonPath("$[1].username", equalTo(username)))
			.andExpect(jsonPath("$[1].applicationId", equalTo(applicationId)))
			.andExpect(jsonPath("$[1].type", equalTo("Important")))
			.andExpect(jsonPath("$[1].message", equalTo("Don't forget to wash your car today")))
			.andExpect(jsonPath("$[1].timestampSent", equalTo(timestampSent.getTime())))
			.andExpect(jsonPath("$[1].timestampReceived", notNullValue()))
			.andExpect(jsonPath("$[1].timestampRead", nullValue()));
		
		//verify if the method called save to update the timestamp received
		verify(notificationRepository, times(1)).save(Matchers.<Collection<Notification>>any());
		
	}
	
	/**
	 * get all the unreceived notifications of an user returns none
	 * @throws Exception
	 */
	@Test
	public void testGetUnreceivedReturningNone() throws Exception {
		String username = "alessandro";
		
		when(notificationRepository.findByUsernameAndNotReceived(username)).thenReturn(new ArrayList<>());
		when(authorizationHelper.getUsername(Matchers.any())).thenReturn(username);
		
		this.mockMvc
		.perform(get(BASE_URL+"/unreceived")
				.accept(Constants.APPLICATION_JSON_UTF8)
				)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(0)));
		
		//verify if the method called save to update the timestamp received
		verify(notificationRepository, never()).save(Matchers.<Collection<Notification>>any());
		
	}
	
	/**
	 * patch the notification by its primary key to mark as read
	 * @throws Exception
	 */
	@Test
	public void testPatchByPrimaryKeyAsRead() throws Exception {
		String applicationId = "my-client-id";
		UUID notificationId = UUIDs.timeBased();
		String username = "alessandro";
		Notification notification = new Notification(notificationId, username, applicationId, new Date(), true, new Date(), false, null, "Alert", "Message content"); //received but not read
		when(notificationRepository.findOne(Matchers.<MapId>any())).thenReturn(notification);
		when(authorizationHelper.getUsername(Matchers.any())).thenReturn(username);
		
		this.mockMvc
		.perform(patch(BASE_URL_WITH_APPLICATION_ID+"/{notificationId}/read", applicationId, notificationId.toString())
				.accept(Constants.APPLICATION_JSON_UTF8)
				)
			.andExpect(status().isOk());
		
		//verify if the method called save to update the timestamp read
		verify(notificationRepository, times(1)).save(Matchers.<Notification>any());
		
	}
	
	/**
	 * patch the notification by its primary key to mark as read by the second time (do nothing)
	 * @throws Exception
	 */
	@Test
	public void testPatchByPrimaryKeyAsReadSecondTime() throws Exception {
		String applicationId = "my-client-id";
		UUID notificationId = UUIDs.timeBased();
		String username = "alessandro";
		Notification notification = new Notification(notificationId, username, applicationId, new Date(), true, new Date(), true, new Date(), "Alert", "Message content");
		when(notificationRepository.findOne(Matchers.<MapId>any())).thenReturn(notification);
		when(authorizationHelper.getUsername(Matchers.any())).thenReturn(username);
		
		this.mockMvc
		.perform(patch(BASE_URL_WITH_APPLICATION_ID+"/{notificationId}/read", applicationId, notificationId.toString())
				.accept(Constants.APPLICATION_JSON_UTF8)
				)
			.andExpect(status().isOk());
		
		//certify that the method save is never called
		verify(notificationRepository, never()).save(Matchers.<Notification>any());
		
	}
	
	/**
	 * patch a non-existent notification by its primary key to mark as read by the second time (not found)
	 * @throws Exception
	 */
	@Test
	public void testPatchByPrimaryKeyAsReadNotFound() throws Exception {
		String applicationId = "my-client-id";
		UUID notificationId = UUIDs.timeBased();
		String username = "alessandro";
		when(notificationRepository.findOne(Matchers.<MapId>any())).thenReturn(null);
		when(authorizationHelper.getUsername(Matchers.any())).thenReturn(username);
		
		this.mockMvc
		.perform(patch(BASE_URL_WITH_APPLICATION_ID+"/{notificationId}/read", applicationId, notificationId.toString())
				.accept(Constants.APPLICATION_JSON_UTF8)
				)
			.andExpect(status().isNotFound());
	}

}
