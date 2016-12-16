package com.amoraesdev.mobify;

import java.io.IOException;

import org.springframework.context.MessageSource;

import com.amoraesdev.mobify.config.LocalizationConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

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
}
