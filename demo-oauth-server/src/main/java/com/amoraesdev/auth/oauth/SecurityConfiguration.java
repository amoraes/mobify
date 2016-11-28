package com.amoraesdev.auth.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security Configuration (in-memory, use only in development environment)
 * @author Alessandro Moraes alessandro(at)amoraesdev.com
 */
@Configuration
@Order(-10)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin().loginPage("/login").permitAll();
		http.requestMatchers()
		.antMatchers("/login", "/logout", "/oauth/authorize");
		http.authorizeRequests()
		.antMatchers("/login", "/logout", "/oauth/authorize").permitAll();
		http.authorizeRequests().anyRequest().authenticated();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/bower_components/**");
		web.ignoring().antMatchers("/imgs/**");
		web.ignoring().antMatchers("/css/**");
		
	}
	
	@Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
			.withUser("admin").roles("ADMIN").password("admin123")
			.and()
			.withUser("user").roles("USER").password("user123");
		
    }


}
