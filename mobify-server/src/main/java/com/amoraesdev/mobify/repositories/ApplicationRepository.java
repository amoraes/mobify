package com.amoraesdev.mobify.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.amoraesdev.mobify.entities.Application;

public interface ApplicationRepository extends CassandraRepository<Application> {
	
	@Query("select * from application where application_id = ?0")
	public Application findByApplicationId(String applicationId);

}
