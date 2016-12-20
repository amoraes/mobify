package com.amoraesdev.mobify.utils;

import java.util.UUID;

import org.springframework.data.cassandra.repository.MapId;
import org.springframework.data.cassandra.repository.support.BasicMapId;

/**
 * Helper class to generate {@link MapId} for our entities
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
public class MapIdGenerator {

	public static MapId createNotificationPK(String id, String username, String applicationId){
		return BasicMapId.id()
				.with("id", UUID.fromString(id))
				.with("username", username)
				.with("applicationId", applicationId);
	}
	
}
