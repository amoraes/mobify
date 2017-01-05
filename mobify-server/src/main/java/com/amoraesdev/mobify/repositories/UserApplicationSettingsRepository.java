package com.amoraesdev.mobify.repositories;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.amoraesdev.mobify.entities.UserApplicationSettings;

public interface UserApplicationSettingsRepository extends CassandraRepository<UserApplicationSettings> {
	
	@Query("select * from user_application_settings where username = ?0")
	public List<UserApplicationSettings> findByUsername(String username);
	
	@Query("select * from user_application_settings where username = ?0 and application_id = ?1")
	public UserApplicationSettings findByUsernameAndApplicationId(String username, String applicationId);

}
