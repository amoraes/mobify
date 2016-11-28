package com.amoraesdev.mobify.entities;

import java.io.Serializable;

import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Represents a user
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Table
public class User implements Serializable {

	private static final long serialVersionUID = 5742664546938147425L;
	
	@PrimaryKey(value = "username")
	private String username;
	
	private String name;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
