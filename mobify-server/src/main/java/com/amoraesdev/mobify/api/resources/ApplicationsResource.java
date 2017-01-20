package com.amoraesdev.mobify.api.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amoraesdev.mobify.api.valueobjects.ApplicationVO;
import com.amoraesdev.mobify.entities.Application;
import com.amoraesdev.mobify.exceptions.NotFoundException;
import com.amoraesdev.mobify.repositories.ApplicationRepository;

/**
 * API endpoint to manipulate {@link Application}
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@RestController
public class ApplicationsResource {
	
	public static final String BASE_URL = "/api/applications";
	
	@Autowired
	private ApplicationRepository applicationRepositoy;
	
	/**
	 * Get a {@link Application} based on the primary key
	 * @param applicationId
	 * @return
	 * @throws NotFoundException 
	 */
	@RequestMapping(path = BASE_URL + "/{applicationId:.+}",method = RequestMethod.GET)
	public ApplicationVO getByPrimaryKey(
			@PathVariable("applicationId") String applicationId) throws NotFoundException{
		Application application = applicationRepositoy.findByApplicationId(applicationId);
		//not found?
		if(application == null){
			throw new NotFoundException("error.entity_not_found", "Application", String.format("[applicationId=%s]", applicationId));
		}
		//populate the value object and return it
		ApplicationVO vo = new ApplicationVO();
		vo.setApplicationId(application.getApplicationId());
		vo.setName(application.getName());
		vo.setIcon(application.getIcon());
		return vo;
	}
	
	

}
