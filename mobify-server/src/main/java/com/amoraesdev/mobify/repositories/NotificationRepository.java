package com.amoraesdev.mobify.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.amoraesdev.mobify.entities.Notification;

public interface NotificationRepository extends CassandraRepository<Notification> {
	
	@Query("select * from notification where username = ?0 and application_id = ?1")
	public List<Notification> findByUsernameAndApplicationId(String username, String applicationId);
	
	@Query("select * from notification where username = ?0 and received = false")
	public List<Notification> findByUsernameAndNotReceived(String username);
	
	@Query("select * from notification where username = ?0 and application_id = ?1 and id = ?2")
	public Notification findByUsernameAndApplicationIdAndId(String username, String applicationId, UUID id);

}
