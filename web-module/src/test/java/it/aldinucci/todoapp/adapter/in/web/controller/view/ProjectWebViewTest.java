package it.aldinucci.todoapp.adapter.in.web.controller.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import it.aldinucci.todoapp.adapter.in.web.controller.CreateTaskWebController;
import it.aldinucci.todoapp.adapter.in.web.controller.ProjectWebController;
import it.aldinucci.todoapp.adapter.in.web.dto.UserWebDto;
import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.LoadTasksByProjectUsePort;
import it.aldinucci.todoapp.application.port.in.LoadUserByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {ProjectWebController.class, CreateTaskWebController.class})
@PropertySource("classpath:messages.properties")
public class ProjectWebViewTest {

	private static final String FIXTURE_EMAIL = "test@email.it";
	
	@Autowired
	private WebClient webClient;
	
	@MockBean
	private LoadProjectsByUserUsePort loadProjects;
	
	@MockBean
	private LoadTasksByProjectUsePort loadTasks;
	
	@MockBean
	private LoadUserByProjectIdUsePort loadUser;
	
	@MockBean
	private AppGenericMapper<User, UserWebDto> mapper;
	
	@MockBean
	private InputModelAuthorization<User> authorize;
	
	@Autowired
	private Environment env;
	
	private List<Project> projects;
	
	private User user;
	
	@BeforeEach
	void setUp() {
		user = new User(FIXTURE_EMAIL, "username", "password");
		projects = Arrays.asList(
				new Project(2L, "test project"),
				new Project(5L, "second project"),
				new Project(7L, "project test"));
		when(loadUser.load(isA(ProjectIdDTO.class))).thenReturn(user);
		when(mapper.map(user)).thenReturn(new UserWebDto("username", FIXTURE_EMAIL));
		when(loadProjects.load(isA(UserIdDTO.class))).thenReturn(projects);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_projectView_shouldContainsAllTask() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		List<Task> tasks = Arrays.asList(
				new Task(11L, "first task", "desc 1"),
				new Task(13L, "second task", "desc 2"));
		
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(tasks);
		
		HtmlPage page = webClient.getPage("/project/5/tasks");
		
		
		HtmlTable table = page.getHtmlElementById("tasks-table");
		
		assertThat(table.getRow(0).getTextContent()).contains("second project");
		
		assertThat(table.getRow(1).getCell(1).getTextContent())
			.contains("first task")
			.contains("desc 1");
		
		assertThat(table.getRow(2).getCell(1).getTextContent())
			.contains("second task")
			.contains("desc 2");
		
//		assertThat(page.getAnchorByHref("/project/5/task/new").getTextContent())
//			.contains(env.getProperty("add.task"));
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_projectView_shouldContrainCreateTaskForm() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(Collections.emptyList());
		
		HtmlPage page = webClient.getPage("/project/5/tasks");
		
		
		HtmlTable table = page.getHtmlElementById("tasks-table");
		
		page.getElementById(FIXTURE_EMAIL);
	}
}
