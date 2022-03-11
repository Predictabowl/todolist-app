package it.aldinucci.todoapp.adapter.in.web.controller;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.util.NestedServletException;

import it.aldinucci.todoapp.adapter.in.web.dto.UserWebDto;
import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.LoadTasksByProjectUsePort;
import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.LoadUserByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@WebMvcTest(controllers = {TaskWebController.class})
@ExtendWith(SpringExtension.class)
class TaskWebControllerTest {

	private static final String FIXTURE_EMAIL = "test@email.it";
	private static final long FIXTURE_PROJECT_ID = 7;
	private static final ProjectIdDTO FIXTURE_PROJECT_ID_DTO = new ProjectIdDTO(FIXTURE_PROJECT_ID);
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private LoadProjectsByUserUsePort loadProjects;
	
	@MockBean
	private LoadUserByProjectIdUsePort loadUser;
	
	@MockBean
	private LoadTasksByProjectUsePort loadTasks;
	
	@MockBean
	private AppGenericMapper<User, UserWebDto> userMapper;
	
	@MockBean
	private InputModelAuthorization<User> authorize;
	
	private List<Project> projects;
	
	private User fixtureUser;
	
	
	@BeforeEach
	void setUp() {
		fixtureUser = new User(FIXTURE_EMAIL, "username", "password", true);
		UserIdDTO userIdDTO = new UserIdDTO(FIXTURE_EMAIL);
		projects = Arrays.asList(
				new Project(FIXTURE_PROJECT_ID, "first project"),
				new Project(3L, "second project"));
		when(loadProjects.load(userIdDTO)).thenReturn(projects);
		when(loadUser.load(FIXTURE_PROJECT_ID_DTO)).thenReturn(fixtureUser);
		when(userMapper.map(fixtureUser)).thenReturn(new UserWebDto("username", FIXTURE_EMAIL));
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_viewTasksWhenNoTasks() throws Exception {
		UserIdDTO userIdDTO = new UserIdDTO(FIXTURE_EMAIL);
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(Collections.emptyList());
		
		mvc.perform(get("/project/"+FIXTURE_PROJECT_ID+"/tasks")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(view().name("index"))
			.andExpect(model().attribute("user", new UserWebDto("username",FIXTURE_EMAIL)))
			.andExpect(model().attribute("projects", projects))
			.andExpect(model().attribute("tasks", Collections.emptyList()));
		
		ProjectIdDTO projectIdDTO = new ProjectIdDTO(2);
		verify(loadUser).load(FIXTURE_PROJECT_ID_DTO);
		verify(loadProjects).load(userIdDTO);
		verify(loadTasks).load(FIXTURE_PROJECT_ID_DTO);
		verify(userMapper).map(fixtureUser);
		verify(authorize).check(FIXTURE_EMAIL, fixtureUser);
	}
	
	@Test
	@WithMockUser("another@user.it")
	void test_viewWithFewTasks_shouldCallAuthorizationCorrectly() throws Exception {
		List<Task> tasks = Arrays.asList(
				new Task(5L, "task 1", "descr 1"),
				new Task(11L, "task 2", "descr 2"));
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(tasks);
		
		mvc.perform(get("/project/"+FIXTURE_PROJECT_ID+"/tasks")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(view().name("index"))
			.andExpect(model().attribute("user", new UserWebDto("username",FIXTURE_EMAIL)))
			.andExpect(model().attribute("projects", projects))
			.andExpect(model().attribute("tasks", tasks));
		
		InOrder inOrder = inOrder(loadUser, loadProjects, loadTasks, userMapper, authorize);
		inOrder.verify(loadUser).load(FIXTURE_PROJECT_ID_DTO);
		inOrder.verify(authorize).check("another@user.it", fixtureUser);
		inOrder.verify(userMapper).map(fixtureUser);
		inOrder.verify(loadProjects).load(new UserIdDTO(FIXTURE_EMAIL));
		inOrder.verify(loadTasks).load(FIXTURE_PROJECT_ID_DTO);
	}
	
	
}
