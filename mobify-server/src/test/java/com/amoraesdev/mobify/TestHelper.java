package com.amoraesdev.mobify;

import java.io.IOException;

import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import com.amoraesdev.mobify.config.LocalizationConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class to help unit testing
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
public class TestHelper {
	
	private static final MessageSource messageSource = new LocalizationConfig().messageSource();
	
	public static MessageSource getDefaultMessageSource(){
		return messageSource;
	}
	
	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

	/**
	 * Utility class to print the RESTful API return and help debugging during tests
	 */
	public static class Printer implements ResultHandler {
		@Override
		public void handle(MvcResult result) throws Exception {
			System.out.println(result.getResponse().getContentAsString());
		}
	}
	
}
