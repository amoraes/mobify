package com.amoraesdev.mobify;

import java.nio.charset.Charset;
import java.util.Locale;

import org.springframework.http.MediaType;

/**
 * Global constants
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
public class Constants {
	
	public static final Locale DEFAULT_API_LOCALE = Locale.ENGLISH;
	
	public static final MediaType APPLICATION_JSON_UTF8 = 
			new MediaType(MediaType.APPLICATION_JSON.getType(),
						MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
}
