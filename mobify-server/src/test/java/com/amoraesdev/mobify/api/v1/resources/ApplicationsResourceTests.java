package com.amoraesdev.mobify.api.v1.resources;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import com.amoraesdev.mobify.Constants;
import com.amoraesdev.mobify.TestHelper;
import com.amoraesdev.mobify.api.resources.ApplicationsResource;
import com.amoraesdev.mobify.entities.Application;
import com.amoraesdev.mobify.repositories.ApplicationRepository;
import com.amoraesdev.mobify.utils.RestErrorsControllerAdvice;

/**
 * {@link ApplicationsResource} unit tests
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
public class ApplicationsResourceTests {

	private MockMvc mockMvc;
	
	private final String BASE_URL = ApplicationsResource.BASE_URL;
	
	@Mock
	private ApplicationRepository applicationRepository;
	
	@InjectMocks
	private ApplicationsResource resource;
	
	/**
	 * Initial configuration
	 */
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		RestErrorsControllerAdvice advice = new RestErrorsControllerAdvice();
		advice.setMessageSource(TestHelper.getDefaultMessageSource());
		this.mockMvc = standaloneSetup(resource)
				.setControllerAdvice(advice)
			.build();
	}
	
	
	/**
	 * get the application by its primary key
	 * @throws Exception
	 */
	@Test
	public void testGetByPrimaryKey() throws Exception {
		String applicationId = "my-client-id";
		String name = "Client App";
		String icon ="iconx";
		Application app = new Application(applicationId, name, icon);
		
		when(applicationRepository.findByApplicationId(applicationId)).thenReturn(app);
		
		this.mockMvc
		.perform(get(BASE_URL+"/{applicationId}", applicationId)
				.accept(Constants.APPLICATION_JSON_UTF8)
				)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.applicationId", equalTo(applicationId)))
			.andExpect(jsonPath("$.name", equalTo(name)))
			.andExpect(jsonPath("$.icon", equalTo(icon)));
		
	}
	
	
	/**
	 * get the application by its primary key (not not found)
	 * @throws Exception
	 */
	@Test
	public void testGetByPrimaryKeyNotFound() throws Exception {
		String applicationId = "my-client-id";
		
		when(applicationRepository.findByApplicationId(applicationId)).thenReturn(null);
		
		this.mockMvc
		.perform(get(BASE_URL+"/{applicationId}", applicationId)
				.accept(Constants.APPLICATION_JSON_UTF8)
				)
			.andExpect(status().isNotFound());
		
	}
	

}
