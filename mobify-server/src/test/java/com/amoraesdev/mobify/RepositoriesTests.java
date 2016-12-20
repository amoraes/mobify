package com.amoraesdev.mobify;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.amoraesdev.mobify.entities.Application;
import com.amoraesdev.mobify.entities.MobileDevice;
import com.amoraesdev.mobify.entities.MobileDevice.Type;
import com.amoraesdev.mobify.entities.Notification;
import com.amoraesdev.mobify.entities.User;
import com.amoraesdev.mobify.repositories.ApplicationRepository;
import com.amoraesdev.mobify.repositories.MobileDeviceRepository;
import com.amoraesdev.mobify.repositories.NotificationRepository;
import com.amoraesdev.mobify.repositories.UserRepository;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;

/**
 * Entities repositories integration tests
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("integration_tests")
@ContextConfiguration
public class RepositoriesTests {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Autowired
	private MobileDeviceRepository mobileDeviceRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	Session session;
	
	/**
	 * Tests {@link UserRepository}
	 */
	@Test
	public void testUserRepository(){
		User user = new User();
		user.setName("Alessandro Moraes");
		user.setUsername("alessandro.moraes");
		userRepository.save(user);
		
		User userRetrieved = userRepository.findByUsername("alessandro.moraes");
		Assert.assertEquals("alessandro.moraes", userRetrieved.getUsername());
		Assert.assertEquals("Alessandro Moraes", userRetrieved.getName());
				
	}
	
	/**
	 * Tests {@link ApplicationRepository}
	 */
	@Test
	public void testApplicationRepository(){
		Application app = new Application();
		app.setApplicationId("monitor-checker");
		app.setIcon("https://www.amoraesdev.com/mobify/icons/monitor-checker.svg");
		app.setName("System Monitor");
		applicationRepository.save(app);
		
		Application appRetrieved = applicationRepository.findByApplicationId("monitor-checker");
		Assert.assertEquals("monitor-checker", appRetrieved.getApplicationId());
		Assert.assertEquals("https://www.amoraesdev.com/mobify/icons/monitor-checker.svg", appRetrieved.getIcon());
		Assert.assertEquals("System Monitor", appRetrieved.getName());		
		
	}
	
	/**
	 * Tests {@link MobileDeviceRepository}
	 */
	@Test
	public void testMobileDeviceRepository(){
		MobileDevice mobileDevice1 = new MobileDevice("alessandro.moraes","abc123",Type.ANDROID);
		MobileDevice mobileDevice2 = new MobileDevice("alessandro.moraes","xyz456",Type.IOS);
		mobileDeviceRepository.save(Arrays.asList(mobileDevice1, mobileDevice2));
						
		List<MobileDevice> listRetrieved = mobileDeviceRepository.findByUsername("alessandro.moraes");
		Assert.assertEquals(listRetrieved.size(), 2);
		Assert.assertEquals("alessandro.moraes", listRetrieved.get(0).getUsername());
		Assert.assertEquals("abc123", listRetrieved.get(0).getDeviceId());
		Assert.assertEquals(Type.ANDROID, listRetrieved.get(0).getType());
		Assert.assertEquals("alessandro.moraes", listRetrieved.get(1).getUsername());
		Assert.assertEquals("xyz456", listRetrieved.get(1).getDeviceId());
		Assert.assertEquals(Type.IOS, listRetrieved.get(1).getType());
		
	}
	
	
	/**
	 * Tests {@link NotificationRepository}
	 */
	@Test
	public void testNotificationRepository() {
		String appId = "monitor-checker";
		String username = "alessandro.moraes";
		Notification notification1 = new Notification(UUIDs.timeBased(), username ,appId, new Date(), true, new Date(), false, null , "Alert", "Server 1 is offline");
		notification1.setTimestampReceived(new Date()); //this notification has been received by the user already
		Notification notification2 = new Notification(UUIDs.timeBased(), username ,appId, new Date(), false, null, false, null, "Message", "Server 2 is online");
		Notification notification3 = new Notification(UUIDs.timeBased(), username ,"other-app", new Date(), false, null, false, null, "Message", "Any message");
		notificationRepository.save(Arrays.asList(notification1,notification2, notification3));
		
		List<Notification> listByApp = notificationRepository.findByUsernameAndApplicationId(username, appId);
		Assert.assertEquals(2, listByApp.size());
		
		//create the secondary index 
		//TODO verify why spring boot is not creating it automatically
		session.execute("CREATE INDEX IF NOT EXISTS notification_received ON notification (received);");
		
		List<Notification> listNotReceivedByUsername = notificationRepository.findByUsernameAndNotReceived(username);
		Assert.assertEquals(2, listNotReceivedByUsername.size());
		
	}
}
