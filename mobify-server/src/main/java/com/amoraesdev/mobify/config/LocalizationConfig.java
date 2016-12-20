package com.amoraesdev.mobify.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.amoraesdev.mobify.Constants;

/**
 * Localization configuration class
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Configuration
public class LocalizationConfig {
	
	@Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Constants.DEFAULT_API_LOCALE);
        return slr;
    }
    
    @Bean
    public ResourceBundleMessageSource messageSource() {
     ResourceBundleMessageSource source = new ResourceBundleMessageSource();
     source.setBasenames("messages/messages");
     return source;
    }
}
