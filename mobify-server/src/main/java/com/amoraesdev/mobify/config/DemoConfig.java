package com.amoraesdev.mobify.config;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amoraesdev.mobify.entities.Application;
import com.amoraesdev.mobify.entities.Notification;
import com.amoraesdev.mobify.repositories.ApplicationRepository;
import com.amoraesdev.mobify.repositories.NotificationRepository;
import com.datastax.driver.core.utils.UUIDs;

/**
 * Demo version configuration class
 * This class will insert some data to be used in the demo, only active in the profile demo
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Configuration
@Profile("demo")
public class DemoConfig {

	@Autowired
	private ApplicationRepository applicationRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@PostConstruct
	public void init(){
		Application app1 = new Application("mobify","Mobify","icon1");
		applicationRepository.save(app1);
		Notification n = new Notification(UUIDs.timeBased(), "user", app1.getName(), new Date(), "Notice", "Welcome to Mobify! If you received this, our demo is working."); 
		notificationRepository.save(n);
		
		Application app2 = new Application("monitor-checker", "Cluster Monitor", "icon2");
		applicationRepository.save(app2);
		n = new Notification(UUIDs.timeBased(), "user", app2.getName(), new Date(), "Alert", "Server 2 is offline.");
		notificationRepository.save(n);
		n = new Notification(UUIDs.timeBased(), "admin", app2.getName(), new Date(), "Alert", "Server 2 is offline.");
		notificationRepository.save(n);
		n = new Notification(UUIDs.timeBased(), "user", app2.getName(), new Date(), "Alert", "Server 3 is offline.");
		notificationRepository.save(n);
		
		Application app3 = new Application("human-resources", "Human Resources", "icon3");
		applicationRepository.save(app3);
		n = new Notification(UUIDs.timeBased(), "user", app3.getName(), new Date(), "Important", "Your vacation dates has been changed by your supervisor.");
		notificationRepository.save(n);
		n = new Notification(UUIDs.timeBased(), "user", app3.getName(), new Date(), "Notice", "Your paylisps are now available.");
		notificationRepository.save(n);
	}
	
}
