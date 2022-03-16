package it.aldinucci.todoapp.adapter.in.web.controller.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import it.aldinucci.todoapp.adapter.in.web.controller.CreateProjectWebController;
import it.aldinucci.todoapp.adapter.in.web.controller.IndexWebController;
import it.aldinucci.todoapp.adapter.in.web.controller.LoginWebController;
import it.aldinucci.todoapp.adapter.in.web.dto.UserWebDto;
import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.NewProjectWebDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {IndexWebController.class})
@PropertySource("classpath:messages.properties")
class IndexWebViewTest {
	
	private static final String FIXTURE_EMAIL = "test@email.it";
	
	@Autowired
	private WebClient webClient;
	
	@MockBean
	private LoadProjectsByUserUsePort loadProjects;
	
	@MockBean
	private LoadUserByEmailUsePort loadUserByEmail;
	
	@MockBean
	private AppGenericMapper<User, UserWebDto> mapper;
	
	@MockBean
	private CreateProjectWebController createProjectWebController;
	
	@MockBean
	private LoginWebController loginWebController;
	
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
		when(loadUserByEmail.load(new UserIdDTO(FIXTURE_EMAIL))).thenReturn(Optional.of(user));
		when(mapper.map(user)).thenReturn(new UserWebDto("username", FIXTURE_EMAIL));
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_indexView_when_thereAreNoProjects() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loadProjects.load(isA(UserIdDTO.class))).thenReturn(Collections.emptyList());
		
		HtmlPage page = webClient.getPage("/");
				
		assertThat(page.getBody().getTextContent())
			.contains(env.getProperty("no.projects"));
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_indexView_when_thereAreProjects() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loadProjects.load(isA(UserIdDTO.class))).thenReturn(projects);
		
		HtmlPage page = webClient.getPage("/");
		
		assertThat(page.getAnchorByHref("/project/2/tasks").getTextContent())
			.matches("test project");
		assertThat(page.getAnchorByHref("/project/5/tasks").getTextContent())
			.matches("second project");
		assertThat(page.getAnchorByHref("/project/7/tasks").getTextContent())
			.matches("project test");
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_indexView_haveNewProjectForm() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loadProjects.load(isA(UserIdDTO.class))).thenReturn(projects);
		when(createProjectWebController.createProject(any(), any(), any())).thenReturn("test-view-page");
		
		HtmlPage page = webClient.getPage("/");
		page.getHtmlElementById("open-new-project-card").click();
		
		HtmlForm form = page.getFormByName("new-project-form");
		form.getInputByName("name").setValueAttribute("Test Project");
		form.getButtonByName("submit-button").click();
		
		verify(createProjectWebController).createProject(
				isA(Authentication.class), 
				eq(new NewProjectWebDto("Test Project")),
				isA(BindingResult.class));
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_logoutForm() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loginWebController.login()).thenReturn("test-view-page");
		
		HtmlPage page = webClient.getPage("/");
		
		HtmlForm logoutForm = page.getFormByName("logout-form");
		assertThat(logoutForm.getActionAttribute()).matches("/logout");
		assertThat(logoutForm.getMethodAttribute()).matches("post");
		
		page.getHtmlElementById("user-menu").mouseOver();
		page.getElementById("logout-link").click();
		
		verify(loginWebController).login();
	}

}
