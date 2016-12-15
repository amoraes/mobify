package com.amoraesdev.mobify.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.amoraesdev.mobify.Constants;
import com.amoraesdev.mobify.exceptions.BusinessLogicException;
import com.amoraesdev.mobify.exceptions.InvalidEntityException;
import com.amoraesdev.mobify.exceptions.NotFoundException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * ControllerAdvice to generate error codes more appropriate
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@ControllerAdvice
public class RestErrorsControllerAdvice extends ResponseEntityExceptionHandler 
{
	private final Logger log = LoggerFactory.getLogger(RestErrorsControllerAdvice.class);
	
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * This field is injected through DI, use this method for unit testing only
	 * @param messageSource
	 */
	public void setMessageSource(MessageSource messageSource){
		this.messageSource = messageSource;
	}
	
	/**
	 * This handler transforms any {@link NotFoundException} thrown by controllers into a 404 HTTP response
	 * @param ex
	 */
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> handleException(NotFoundException ex){
		log.debug("Handling NotFoundException: "+ex.getMessage());
		return ResponseEntity.notFound().build();
	}
	
	/**
	 * This handler transforms any {@link IllegalArgumentException} thrown by controllers into a 400 HTTP response
	 * @param ex
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleException(IllegalArgumentException ex){
		log.debug("Handling "+ex.getClass().getSimpleName()+":"+ex.getMessage());
		ErrorResource error = new ErrorResource();
        error.setCode(ex.getClass().getSimpleName());
        error.setMessage(messageSource.getMessage(ex.getMessage(), null, null));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(Constants.APPLICATION_JSON_UTF8);
        return ResponseEntity.badRequest().headers(headers).body(error);
	}
	
	/**
	 * This handler transforms any {@link BusinessLogicException} thrown by controllers into a 400 HTTP response
	 * @param ex
	 */
	@ExceptionHandler(BusinessLogicException.class)
	public ResponseEntity<?> handleException(BusinessLogicException ex){
		log.debug("Handling "+ex.getClass().getSimpleName()+":"+ex.getMessage());
		ErrorResource error = new ErrorResource();
        error.setCode(ex.getClass().getSimpleName());
        error.setMessage(messageSource.getMessage(ex.getMessage(), null, null));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(Constants.APPLICATION_JSON_UTF8);
        return ResponseEntity.badRequest().headers(headers).body(error);
	}
	

	/**
	 * This handler transforms any {@link InvalidRequestException} thrown by controllers into a 422 HTTP response (Unprocessable Entity)
	 * @param ex
	 * @throws IOException 
	 */
	@ExceptionHandler(InvalidEntityException.class)
    @ResponseBody
    public ResponseEntity<?> handleException(InvalidEntityException ex, HttpServletRequest request) {
		log.debug("Handling InvalidEntityException: "+ex.getMessage()+" (Field Errors: "+ex.getErrors().getFieldErrors().size()+")");
		List<FieldErrorResource> fieldErrorResources = new ArrayList<>();
        List<FieldError> fieldErrors = ex.getErrors().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            FieldErrorResource fieldErrorResource = new FieldErrorResource();
            String objectName = fieldError.getObjectName();
            if(objectName.endsWith("VO")){
            	objectName = objectName.substring(0, objectName.length()-2);
            }
            fieldErrorResource.setResource(objectName);
            fieldErrorResource.setField(fieldError.getField());
            fieldErrorResource.setCode(fieldError.getCode());
            fieldErrorResource.setMessage(fieldError.getDefaultMessage());
            fieldErrorResources.add(fieldErrorResource);
        }
        ErrorResource error = new ErrorResource();
        error.setCode(InvalidEntityException.class.getSimpleName());
        error.setMessage(ex.getMessage());
        error.setFieldErrors(fieldErrorResources);     
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(Constants.APPLICATION_JSON_UTF8);
        
        return ResponseEntity.unprocessableEntity().headers(headers).body(error);
    }
	
}


/**
 * Error message in JSON format
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value=Include.NON_EMPTY)
class ErrorResource {
	
	private String code;
    private String message;
    private List<FieldErrorResource> fieldErrors;

    public ErrorResource() { }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<FieldErrorResource> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldErrorResource> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
  
}

/**
 * Specific field errors representation
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class FieldErrorResource {
	
	private String resource;
    private String field;
    private String code;
    private String message;
    
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
    
}