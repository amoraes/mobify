package com.amoraesdev.mobify.utils;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

/**
 * OAuth helper functions
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Component
public class AuthorizationHelper {
	
	/**
	 * Verifies if the user has an access role
	 * @param role
	 * @return
	 */
	public boolean hasRole(String role) {
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities();
		boolean hasRole = false;
		for (GrantedAuthority authority : authorities) {
			hasRole = authority.getAuthority().equals(role);
			if (hasRole) {
				break;
			}
		}
		return hasRole;
	}
	
	/**
	 * Verifies if the authentication was made by a client (Client Credentials flow)
	 * @return
	 */
	public boolean isClientOnly(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null && auth instanceof OAuth2Authentication){
			OAuth2Authentication oauth = (OAuth2Authentication)auth;
			if(oauth.getUserAuthentication() != null && oauth.getUserAuthentication() instanceof UsernamePasswordAuthenticationToken){
				UsernamePasswordAuthenticationToken upt = (UsernamePasswordAuthenticationToken)oauth.getUserAuthentication();
				HashMap<Object, Object> details = (HashMap<Object, Object>) upt.getDetails();
				boolean clientOnly = (Boolean)details.get("clientOnly");
				return clientOnly;
			}
		}
		return false;
	}
	
	/**
	 * Return the client name (works only with Client Credentials authentications)
	 * @return
	 */
	public String getClientId(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(isClientOnly()){
			return ((OAuth2Authentication)auth).getName();
		}
		return null;
	}
	
	/**
	 * Return the username currently logged in
	 * This method make the code on controllers easier to unit test
	 */
	public String getUsername(Principal user){
		return user.getName();
	}
}
