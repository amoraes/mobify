package com.amoraesdev.mobify.exceptions;

/**
 * Entity not found exception
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
public class NotFoundException extends Exception {

	private static final long serialVersionUID = 2229435632773433813L;
	
	private String entityName;
	private String key;
	
	public NotFoundException(String message, String entityName, String key){
		super(message);
		this.entityName = entityName;
		this.key = key;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}