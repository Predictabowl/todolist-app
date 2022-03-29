package it.aldinucci.todoapp.adapter.in.web.view;

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
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import it.aldinucci.todoapp.adapter.in.web.controller.CreateProjectWebController;
import it.aldinucci.todoapp.adapter.in.web.controller.IndexWebController;
import it.aldinucci.todoapp.adapter.in.web.controller.LoginWebController;
import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.ProjectDataWebDto;
import it.aldinucci.todoapp.webcommons.dto.UserWebDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {IndexWebController.class})
@PropertySource("classpath:messages.properties")
class IndexWebViewTest {
	
	private static final String BASE_URL = "/web";

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
				new Project("2L", "test project"),
				new Project("5L", "second project"),
				new Project("7L", "project test"));
		when(loadUserByEmail.load(new UserIdDTO(FIXTURE_EMAIL))).thenReturn(Optional.of(user));
		when(mapper.map(user)).thenReturn(new UserWebDto("username", FIXTURE_EMAIL));
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_indexView_when_thereAreNoProjects() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loadProjects.load(isA(UserIdDTO.class))).thenReturn(Collections.emptyList());
		
		HtmlPage page = webClient.getPage(BASE_URL);
				
		assertThat(page.getHtmlElementById("sidebar").getTextContent())
			.contains(env.getProperty("no.projects"));
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_indexView_when_thereAreProjects() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loadProjects.load(isA(UserIdDTO.class))).thenReturn(projects);
		
		HtmlPage page = webClient.getPage(BASE_URL);
		HtmlElement sidebar = page.getHtmlElementById("sidebar");
		assertThat(sidebar.isDisplayed()).isTrue();
		
		HtmlAnchor project1 = page.getAnchorByHref("/web/project/2L/tasks");
		assertThat(sidebar.isAncestorOf(project1)).isTrue();
		assertThat(project1.getTextContent()).matches("test project");
		
		HtmlAnchor project2 = page.getAnchorByHref("/web/project/5L/tasks");
		assertThat(sidebar.isAncestorOf(project2)).isTrue();
		assertThat(project2.getTextContent()).matches("second project");
		
		HtmlAnchor project3 = page.getAnchorByHref("/web/project/7L/tasks");
		assertThat(sidebar.isAncestorOf(project3)).isTrue();
		assertThat(project3.getTextContent()).matches("project test");
		
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_indexView_haveNewProjectForm() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loadProjects.load(isA(UserIdDTO.class))).thenReturn(projects);
		when(createProjectWebController.createProject(any(), any(), any())).thenReturn("test-view-page");
		
		HtmlPage page = webClient.getPage(BASE_URL);
		page.getHtmlElementById("open-new-project-card").click();
		
		HtmlForm form = page.getFormByName("new-project-form");
		form.getInputByName("name").setValueAttribute("Test Project");
		form.getButtonByName("submit-button").click();
		
		verify(createProjectWebController).createProject(
				isA(Authentication.class), 
				eq(new ProjectDataWebDto("Test Project")),
				isA(BindingResult.class));
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_logoutForm() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loginWebController.login()).thenReturn("test-view-page");
		
		HtmlPage page = webClient.getPage(BASE_URL);
		
		HtmlForm logoutForm = page.getFormByName("logout-form");
		assertThat(logoutForm.getActionAttribute()).matches("/logout");
		assertThat(logoutForm.getMethodAttribute()).matches("post");
		
		DomElement logoutLink = page.getElementById("logout-link");
		assertThat(logoutLink.isDisplayed()).isFalse();
		page.getHtmlElementById("user-menu").mouseOver();
		logoutLink.click();
		
		verify(loginWebController).login();
	}
	
	
	/*
	 * Testing animation visbility doesn't work with jquery toggle("slow")
	 * it work with "fast" and with instant.
	 * It can be tested with Selenium though, so will be tested in E2E
	 */
}
