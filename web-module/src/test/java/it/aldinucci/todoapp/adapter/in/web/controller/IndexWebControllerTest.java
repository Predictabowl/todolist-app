package it.aldinucci.todoapp.adapter.in.web.controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.util.NestedServletException;

import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.UserWebDto;

@WebMvcTest(controllers = { IndexWebController.class })
@ExtendWith(SpringExtension.class)
class IndexWebControllerTest {

	private static final String FIXTURE_EMAIL = "email@test.it";

	@MockBean
	private LoadProjectsByUserUsePort loadProjects;

	@MockBean
	private LoadUserByEmailUsePort loadUser;

	@MockBean
	private AppGenericMapper<User, UserWebDto> mapper;

	@Autowired
	private MockMvc mvc;
	
	@SpyBean
	private IndexWebController sut;

	private User user;

	@BeforeEach
	void setUp() {
	}

	@Test
	@WithMockUser(username = FIXTURE_EMAIL)
	void test_indexController_withNoProjects() throws Exception {
		user = new User(FIXTURE_EMAIL, "username", "password");
		UserIdDTO userIdDTO = new UserIdDTO(FIXTURE_EMAIL);
		UserWebDto userWebDto = new UserWebDto("username", FIXTURE_EMAIL);
		when(loadUser.load(isA(UserIdDTO.class))).thenReturn(Optional.of(user));
		when(loadProjects.load(isA(UserIdDTO.class))).thenReturn(Collections.emptyList());
		when(mapper.map(isA(User.class))).thenReturn(userWebDto);

		mvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"))
			.andExpect(model().attribute("user", userWebDto))
			.andExpect(model().attribute("projects", Collections.emptyList()));
		
		InOrder inOrder = inOrder(loadProjects, loadUser, mapper);
		inOrder.verify(loadProjects).load(userIdDTO);
		inOrder.verify(loadUser).load(userIdDTO);
		inOrder.verify(mapper).map(user);
	}
	
	@Test
	@WithMockUser(username = FIXTURE_EMAIL)
	void test_indexController_withFewProjects() throws Exception {
		user = new User(FIXTURE_EMAIL, "username", "password");
		UserIdDTO userIdDTO = new UserIdDTO(FIXTURE_EMAIL);
		UserWebDto userWebDto = new UserWebDto("username", FIXTURE_EMAIL);
		when(loadUser.load(isA(UserIdDTO.class))).thenReturn(Optional.of(user));
		List<Project> projects = Arrays.asList(
				new Project(2L, "test project"),
				new Project(7L, "different test"));
		when(loadProjects.load(isA(UserIdDTO.class))).thenReturn(projects);
		when(mapper.map(isA(User.class))).thenReturn(userWebDto);

		mvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"))
			.andExpect(model().attribute("user", userWebDto))
			.andExpect(model().attribute("projects", projects));
		
		InOrder inOrder = inOrder(loadProjects, loadUser, mapper);
		inOrder.verify(loadProjects).load(userIdDTO);
		inOrder.verify(loadUser).load(userIdDTO);
		inOrder.verify(mapper).map(user);
	}
	
	@Test
	@WithMockUser(username = FIXTURE_EMAIL)
	void test_indexController_whenLoadUserFails() throws Exception {
		user = new User(FIXTURE_EMAIL, "username", "password");
		UserIdDTO userIdDTO = new UserIdDTO(FIXTURE_EMAIL);
		when(loadUser.load(isA(UserIdDTO.class))).thenReturn(Optional.empty());
		when(loadProjects.load(isA(UserIdDTO.class))).thenReturn(Collections.emptyList());
		
		MockHttpServletRequestBuilder requestBuilder = get("/");
		
		assertThatThrownBy(() -> mvc.perform(requestBuilder))
			.isInstanceOf(NestedServletException.class)
			.getCause()
				.isInstanceOf(AppUserNotFoundException.class)
				.hasMessage("Critical error: could not find user with email: "+FIXTURE_EMAIL);
		
		verify(loadProjects).load(userIdDTO);
		verify(loadUser).load(userIdDTO);
		verifyNoInteractions(mapper);
	}
	
	@Test
	void test_indexController_withoutAuthorization() throws Exception {
		mvc.perform(get("/"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/login"));
		
		verifyNoInteractions(sut);
	}

}
