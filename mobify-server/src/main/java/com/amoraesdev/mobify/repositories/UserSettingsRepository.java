package com.amoraesdev.mobify.repositories;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.amoraesdev.mobify.entities.UserSettings;

public interface UserSettingsRepository extends CassandraRepository<UserSettings> {
	
	@Query("select * from application_user_settings where username = ?0")
	public List<UserSettings> findByUsername(String username);
	
	@Query("select * from application_user_settings where username = ?0 and application_id = ?1")
	public UserSettings findByUsernameAndApplicationId(String username, String applicationId);

}
