package it.aldinucci.todoapp.adapter.in.rest.controller;

import static it.aldinucci.todoapp.webcommons.config.AppBaseUrls.BASE_REST_URL;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;
import it.aldinucci.todoapp.webcommons.config.security.AppRestSecurityConfig;

@WebMvcTest(controllers = {LoadProjectsByUserRestController.class})
@ExtendWith(SpringExtension.class)
@Import(AppRestSecurityConfig.class)
class LoadProjectsByUserRestControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private LoadProjectsByUserUsePort loadProjects;
	
	@Test
	@WithMockUser("test@email")
	void test_loadProjects_successful() throws Exception {
		Project project1 = new Project(2L, "test project");
		Project project2 = new Project(5L, "another test project");
		when(loadProjects.load(isA(UserIdDTO.class)))
			.thenReturn(asList(project1,project2));
		
		mvc.perform(get(BASE_REST_URL+"/projects")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].id", is(2)))
			.andExpect(jsonPath("$[0].name", is("test project")))
			.andExpect(jsonPath("$[1].id", is(5)))
			.andExpect(jsonPath("$[1].name", is("another test project")));
		
		verify(loadProjects).load(new UserIdDTO("test@email"));
		verifyNoMoreInteractions(loadProjects);
	}

	@Test
	@WithMockUser("test@email")
	void test_loadProjects_whenUserNotFound_shouldReturnBadRequest() throws Exception {
		when(loadProjects.load(isA(UserIdDTO.class)))
			.thenThrow(new AppUserNotFoundException("test user not found"));
		
		mvc.perform(get(BASE_REST_URL+"/projects")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$", is("test user not found")));
		
		verify(loadProjects).load(new UserIdDTO("test@email"));
		verifyNoMoreInteractions(loadProjects);
	}
}
