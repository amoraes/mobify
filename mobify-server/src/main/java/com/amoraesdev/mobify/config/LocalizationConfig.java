package com.amoraesdev.mobify.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class LocalizationConfig {
	
	@Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }
    
    @Bean
    public ResourceBundleMessageSource messageSource() {
     ResourceBundleMessageSource source = new ResourceBundleMessageSource();
     source.setBasenames("messages/messages");
     return source;
    }
}
