package com.amoraesdev.mobify.api.v1.resources;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amoraesdev.mobify.api.v1.valueobjects.ApplicationVO;
import com.amoraesdev.mobify.api.v1.valueobjects.UserSettingsVO;
import com.amoraesdev.mobify.entities.Application;
import com.amoraesdev.mobify.entities.User;
import com.amoraesdev.mobify.entities.UserSettings;
import com.amoraesdev.mobify.exceptions.NotFoundException;
import com.amoraesdev.mobify.repositories.ApplicationRepository;
import com.amoraesdev.mobify.repositories.UserSettingsRepository;
import com.amoraesdev.mobify.utils.AuthorizationHelper;

/**
 * API endpoint to manipulate {@link UserSettings}
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@RestController
public class UserSettingsResource {
	
	public static final String BASE_URL = "/api/v1/user/settings";
	
	@Autowired
	private ApplicationRepository applicationRepositoy;
	
	@Autowired 
	private UserSettingsRepository userSettingsRepository;
	
	@Autowired
	private AuthorizationHelper authorizationHelper;
	
	/**
	 * Get all {@link UserSettings} of the authenticated {@link User}
	 * @return
	 */
	@RequestMapping(path = BASE_URL, method = RequestMethod.GET)
	public Collection<UserSettingsVO> getAllByUser(Principal user) throws NotFoundException{
		String username = authorizationHelper.getUsername(user);
		Collection<UserSettings> listSettings = userSettingsRepository
				.findByUsername(username);
		Collection<UserSettingsVO> listVOs = new ArrayList<>();
		for(UserSettings settings : listSettings) {
			Application application = applicationRepositoy.findByApplicationId(settings.getApplicationId());
			//not found?
			if(application == null){
				throw new NotFoundException("error.entity_not_found", "Application", String.format("[applicationId=%s]", settings.getApplicationId()));
			}
			//populate the value object and return it
			ApplicationVO applicationVO = new ApplicationVO();
			applicationVO.setApplicationId(application.getApplicationId());
			applicationVO.setName(application.getName());
			applicationVO.setIcon(application.getIcon());
			//create the settings vo
			UserSettingsVO vo = new UserSettingsVO();
			vo.setUsername(username);
			vo.setApplication(applicationVO);
			listVOs.add(vo);
		}
		return listVOs;
	}
	
	

}
