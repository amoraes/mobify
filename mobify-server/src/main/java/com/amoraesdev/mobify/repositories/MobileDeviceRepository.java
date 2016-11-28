package com.amoraesdev.mobify.repositories;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.amoraesdev.mobify.entities.MobileDevice;

public interface MobileDeviceRepository extends CassandraRepository<MobileDevice> {

	@Query("select * from mobile_device where username = ?0")
	public List<MobileDevice> findByUsername(String username);
	
}
