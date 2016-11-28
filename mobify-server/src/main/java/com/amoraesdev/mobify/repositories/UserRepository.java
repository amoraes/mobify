package com.amoraesdev.mobify.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.amoraesdev.mobify.entities.User;

public interface UserRepository extends CassandraRepository<User> {

	@Query("select * from user where username = ?0")
	public User findByUsername(String username);
	
}
