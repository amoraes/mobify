package com.amoraesdev.mobify.exceptions;

import java.lang.annotation.Annotation;

import javax.validation.Valid;

import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Exception for binding errors ({@link Valid})
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
public class InvalidEntityException extends RuntimeException {
	
	private static final long serialVersionUID = 3404217895647005909L;
	
	private String entityName;
	private Errors errors;

    public InvalidEntityException(String entityName, Errors errors) {
    	this.entityName = entityName;
        this.errors = errors;
    }
    
    public InvalidEntityException(Class klass, Errors errors){
    	this(getEntityNameFromVO(klass), errors);
    }

    public String getEntityName() { return entityName; }
    
    public Errors getErrors() { return errors; }
    
    private static String getEntityNameFromVO(Class klass){
    	Annotation ann = klass.getAnnotation(JsonTypeName.class);
    	if(ann == null){
    		throw new RuntimeException("Add the annotation @JsonTypeName in class "+klass.getName());
    	}
    	return ((JsonTypeName)ann).value();
    }
    
    
}