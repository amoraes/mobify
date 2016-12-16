package com.amoraesdev.mobify.api.v1.resources;

import static com.amoraesdev.mobify.TestHelper.convertObjectToJsonBytes;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
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

public class NotificationsResourceTests {

	private MockMvc mockMvc;
	
	/**
	 * Base url for every call on this resource
	 */
	private final String BASE_URL = "/api/v1/notifications";
	
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
	 * post as regular user: 
	 * verify if the notification is correctly inserted in the database and sent to the users
	 */	
	@Test
	public void testPostAsRegularUser() throws Exception{
		NotificationPostVO postVO = new NotificationPostVO();
		postVO.setApplicationId("my-client-id");
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
			.perform(post(BASE_URL)
					.contentType(Constants.APPLICATION_JSON_UTF8)
					.content(convertObjectToJsonBytes(postVO))	
					)
				.andExpect(status().isOk());
		//verify if saved and sent two notifications, one for each username
		verify(notificationRepository, times(2)).save(Matchers.<Notification>any());
		verify(senderService, times(2)).sendNotification(Matchers.<Notification>any());
	}
	
	/**
	 * post as regular user without application id (the default is the client id): 
	 * verify if the notification is correctly inserted in the database and sent to the users
	 */	
	@Test
	public void testPostAsRegularUserWithoutApplicationId() throws Exception{
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
			.perform(post(BASE_URL)
					.contentType(Constants.APPLICATION_JSON_UTF8)
					.content(convertObjectToJsonBytes(postVO))	
					)
				.andExpect(status().isOk());
		//verify if saved and sent two notifications, one for each username
		verify(notificationRepository, times(2)).save(Matchers.<Notification>any());
		verify(senderService, times(2)).sendNotification(Matchers.<Notification>any());	
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
			.perform(post(BASE_URL)
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
		postVO.setApplicationId("my-client-id");
		postVO.setMessage(null); //message is a required field
		postVO.setType("Alert");
		postVO.setUsernames("amoraes");
			
		when(authorizationHelper.hasRole(ROLE_MASTER)).thenReturn(false);
		when(authorizationHelper.getClientId()).thenReturn("my-client-id");
		this.mockMvc
			.perform(post(BASE_URL)
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
		postVO.setApplicationId("other-client-id");
		postVO.setMessage("This is a special notification message wrote for you"); //message is a required field
		postVO.setType("Important");
		postVO.setUsernames("amoraes");
			
		when(authorizationHelper.hasRole(ROLE_MASTER)).thenReturn(false);
		when(authorizationHelper.getClientId()).thenReturn("my-client-id");
		this.mockMvc
			.perform(post(BASE_URL)
					.contentType(Constants.APPLICATION_JSON_UTF8)
					.content(convertObjectToJsonBytes(postVO))	
					)
				.andExpect(status().isBadRequest());
		//verify if never saved or sent any notifications
		verify(notificationRepository, never()).save(Matchers.<Notification>any());
		verify(senderService, never()).sendNotification(Matchers.<Notification>any());	
	}
	

}
