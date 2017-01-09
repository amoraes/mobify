package com.amoraesdev.mobify.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.datastax.driver.core.Session;

/**
 * Secondary indexes configuration
 * TODO verify why spring boot is not creating then automatically
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Configuration
public class SecondaryIndexesConfig {

	@Autowired
	private Session session;
	
	@PostConstruct
	public void createSecondaryIndexes(){
		//received index on notification
		session.execute("CREATE INDEX IF NOT EXISTS notification_received ON notification (received);");
	}
	
}
