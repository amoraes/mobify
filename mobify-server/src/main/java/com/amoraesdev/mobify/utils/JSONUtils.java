package com.amoraesdev.mobify.utils;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON utilities class
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 *
 */
public class JSONUtils {
	public static String convertObjectToJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(object);
    }
}
