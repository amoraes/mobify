package com.amoraesdev.mobify.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security configuration class
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Configuration
//@Order(-10)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().authenticated();
	}
	
	/*@Override
	protected void configure(HttpSecurity http) throws Exception {
		super()
		//http.requestMatchers().antMatchers("/ws/**");
		http.
			authorizeRequests()
			.antMatchers("/ws/notifications/**").permitAll()
			.antMatchers("/ws/notifications").permitAll()
			.anyRequest().authenticated();	
	}*/

	@Override
	public void configure(WebSecurity web) throws Exception {
		//web.ignoring().antMatchers("/ws/notifications**");
		web.ignoring().antMatchers("/ws/notifications","/ws/notifications/**");
	}
	
}
