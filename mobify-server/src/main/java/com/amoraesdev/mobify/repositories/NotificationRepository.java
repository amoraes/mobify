package com.amoraesdev.mobify.repositories;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.amoraesdev.mobify.entities.Notification;

public interface NotificationRepository extends CassandraRepository<Notification> {
	
	@Query("select * from notification where username = ?0 and application_id = ?1")
	public List<Notification> findByUsernameAndApplicationId(String username, String applicationId);
	
	

}
