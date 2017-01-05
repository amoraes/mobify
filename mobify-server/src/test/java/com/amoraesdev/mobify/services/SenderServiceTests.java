package com.amoraesdev.mobify.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.amoraesdev.mobify.entities.UserApplicationSettings;
import com.amoraesdev.mobify.entities.Notification;
import com.amoraesdev.mobify.repositories.UserApplicationSettingsRepository;
import com.datastax.driver.core.utils.UUIDs;

/**
 * {@link SenderService} unit tests
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
public class SenderServiceTests {

	@InjectMocks
	private SenderService senderService;
	
	@Mock
	private UserApplicationSettingsRepository userSettingsRepository;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	/**
	 * Verify if new settings are added for this user/application (first notification for this app)
	 * @throws Exception
	 */
	@Test
	public void testSendNotificationNoPreviousSettings() throws Exception {
		String username = "alessandro.moraes";
		String applicationId = "cluster-monitor";
		Notification notification = new Notification(UUIDs.timeBased(), username, 
				applicationId, new Date(), "Alert", "Server 1 is offline");
		when(userSettingsRepository
				.findByUsernameAndApplicationId(username, applicationId)).thenReturn(null);
		senderService.sendNotification(notification);
		//verify if saved a settings for this user/application
		verify(userSettingsRepository, times(1)).save(Matchers.<UserApplicationSettings>any());
	}
	
	/**
	 * Verify if no new settings are added
	 * @throws Exception
	 */
	@Test
	public void testSendNotificationWithPreviousSettings() throws Exception {
		String username = "alessandro.moraes";
		String applicationId = "cluster-monitor";
		Notification notification = new Notification(UUIDs.timeBased(), username, 
				applicationId, new Date(), "Alert", "Server 1 is offline");
		when(userSettingsRepository
				.findByUsernameAndApplicationId(username, applicationId)).thenReturn(new UserApplicationSettings(username, applicationId, false));
		senderService.sendNotification(notification);
		//verify if didn't saved a settings for this user/application		
		verify(userSettingsRepository, times(0)).save(Matchers.<UserApplicationSettings>any());
	}
	
}
