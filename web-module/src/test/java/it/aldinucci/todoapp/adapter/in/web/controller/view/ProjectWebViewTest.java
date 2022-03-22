package it.aldinucci.todoapp.adapter.in.web.controller.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
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
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;

import it.aldinucci.todoapp.adapter.in.web.controller.CreateTaskWebController;
import it.aldinucci.todoapp.adapter.in.web.controller.DeleteProjectWebController;
import it.aldinucci.todoapp.adapter.in.web.controller.DeleteTaskWebController;
import it.aldinucci.todoapp.adapter.in.web.controller.ProjectWebController;
import it.aldinucci.todoapp.adapter.in.web.controller.UpdateProjectWebController;
import it.aldinucci.todoapp.adapter.in.web.controller.UpdateTaskWebController;
import it.aldinucci.todoapp.application.port.in.LoadProjectsByUserUsePort;
import it.aldinucci.todoapp.application.port.in.LoadTasksByProjectUsePort;
import it.aldinucci.todoapp.application.port.in.LoadUserByProjectIdUsePort;
import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.ProjectDataWebDto;
import it.aldinucci.todoapp.webcommons.dto.TaskDataWebDto;
import it.aldinucci.todoapp.webcommons.dto.UserWebDto;
import it.aldinucci.todoapp.webcommons.security.authorization.InputModelAuthorization;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {ProjectWebController.class})
@PropertySource("classpath:messages.properties")
class ProjectWebViewTest {

	private static final String TEST_VIEW_PAGE = "test-view-page";

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
	private InputModelAuthorization<User> authorizeUser;
	
	@MockBean
	private CreateTaskWebController createTaskController;
	
	@MockBean
	private UpdateProjectWebController updateProjectController;
	
	@MockBean
	private DeleteProjectWebController deleteProjectController;
	
	@MockBean
	private DeleteTaskWebController deleteTaskController;
	
	@MockBean
	private UpdateTaskWebController updateTaskController;
	
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
		when(loadUser.load(isA(ProjectIdDTO.class))).thenReturn(Optional.of(user));
		when(mapper.map(user)).thenReturn(new UserWebDto("username", FIXTURE_EMAIL));
		when(loadProjects.load(isA(UserIdDTO.class))).thenReturn(projects);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_projectView_shouldContainsAllTask() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		List<Task> tasks = Arrays.asList(
				new Task(11L, "first task", "desc 1", false),
				new Task(13L, "second task", "desc 2", false),
				new Task(15L, "third task", "desc 3", true));
		
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(tasks);
		
		HtmlPage page = webClient.getPage("/web/project/5/tasks");
		
		assertThat(page.getHtmlElementById("active-project-section").getTextContent())
			.contains("second project");
		
		HtmlUnorderedList taskList = page.getHtmlElementById("tasks-list");
		assertThat(taskList.getTextContent())
			.contains("first task")
			.contains("desc 1")
			.contains("second task")
			.contains("desc 2")
			.doesNotContain("third task")
			.doesNotContain("desc 3");

		HtmlUnorderedList completedTaskList = page.getHtmlElementById("completed-tasks-list");
		assertThat(completedTaskList.getTextContent())
			.doesNotContain("first task")
			.doesNotContain("desc 1")
			.doesNotContain("second task")
			.doesNotContain("desc 2")
			.contains("third task")
			.contains("desc 3");
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_projectView_CreateTask() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(Collections.emptyList());
		when(createTaskController.createNewTask(any(),any(), any(), any())).thenReturn(TEST_VIEW_PAGE);
		
		HtmlPage page = webClient.getPage("/web/project/5/tasks");
		page.getElementById("add-task-link").click();
		HtmlForm form = page.getFormByName("new-task-form");
		form.getInputByName("name").setValueAttribute("test task");
		form.getTextAreaByName("description").setText("Test description\nNewline");
		HtmlButton formButton = form.getButtonByName("submit-button");

		assertThat(formButton.getTextContent()).matches(env.getProperty("add"));
		assertThat(formButton.getType()).matches("submit");
		formButton.click();
	
		verify(createTaskController).createNewTask(
				isA(Authentication.class),
				eq(new ProjectIdDTO(5)),
				eq(new TaskDataWebDto("test task", "Test description\r\nNewline")),
				isA(BindingResult.class));
		
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_projectView_closeCreateTaskForm() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(Collections.emptyList());
		
		HtmlPage page = webClient.getPage("/web/project/5/tasks");
		page.getElementById("add-task-link").click();
		HtmlForm form = page.getFormByName("new-task-form");
		HtmlButton formButton = form.getButtonByName("cancel-button");
		

		assertThat(formButton.getTextContent()).matches(env.getProperty("cancel"));
		assertThat(formButton.getType()).matches("button");
		formButton.click();
		
		assertThat(form.getVisibleText()).isEmpty();
	
		verifyNoInteractions(createTaskController);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_changeTaskCompletedStatus() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		List<Task> tasks = Arrays.asList(
				new Task(11L, "first task", "desc 1", false, 1),
				new Task(13L, "second task", "desc 2", false, 2),
				new Task(15L, "third task", "desc 3", true, 3));
		
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(tasks);
		
		HtmlPage page = webClient.getPage("/web/project/5/tasks");
		
		HtmlForm form13 = page.getFormByName("complete-task-13-form");
		assertThat(form13.getActionAttribute()).matches("/web/project/5/task/13/toggle/completed");
		assertThat(form13.getButtonByName("submit-button")).isNotNull();
		assertThat(form13.getMethodAttribute()).matches("post");
		
		HtmlForm form11 = page.getFormByName("complete-task-11-form");
		assertThat(form11.getActionAttribute()).matches("/web/project/5/task/11/toggle/completed");
		assertThat(form11.getButtonByName("submit-button")).isNotNull();
		assertThat(form13.getMethodAttribute()).matches("post");
		
		HtmlForm form15 = page.getFormByName("complete-task-15-form");
		assertThat(form15.getActionAttribute()).matches("/web/project/5/task/15/toggle/completed");
		assertThat(form15.getButtonByName("submit-button")).isNotNull();
		assertThat(form13.getMethodAttribute()).matches("post");
		
		HtmlUnorderedList tasksDom = page.getHtmlElementById("tasks-list");
		assertThat(tasksDom.isAncestorOf(form11)).isTrue();
		assertThat(tasksDom.isAncestorOf(form13)).isTrue();
		assertThat(tasksDom.isAncestorOf(form15)).isFalse();
		
		HtmlUnorderedList completedTasksDom = page.getHtmlElementById("completed-tasks-list");
		assertThat(completedTasksDom.isAncestorOf(form11)).isFalse();
		assertThat(completedTasksDom.isAncestorOf(form13)).isFalse();
		assertThat(completedTasksDom.isAncestorOf(form15)).isTrue();
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_visibility_ofCompletedTasks() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(Collections.emptyList());
		
		HtmlPage page = webClient.getPage("/web/project/5/tasks");
		
		assertThat(page.getHtmlElementById("completed-tasks").isDisplayed()).isFalse();
		page.getHtmlElementById("activeProject-menu-trigger").mouseOver();
		page.getHtmlElementById("toggle-completed-tasks").click();
		assertThat(page.getHtmlElementById("completed-tasks").isDisplayed()).isTrue();
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_updateProject() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(Collections.emptyList());
		when(updateProjectController.updateProjectWebEndPoint(any(), anyLong(), any(), any()))
			.thenReturn(TEST_VIEW_PAGE);
		
		HtmlPage page = webClient.getPage("/web/project/5/tasks");
		
		HtmlForm form = page.getFormByName("activeProject-edit-form");
		assertThat(form.isDisplayed()).isFalse();
		
		page.getHtmlElementById("activeProject-menu-trigger").mouseOver();
		HtmlElement triggerLink = page.getHtmlElementById("activeProject-edit-trigger");
		assertThat(triggerLink.getTextContent()).matches(env.getProperty("edit.project"));
		triggerLink.click();
		
		assertThat(form.isDisplayed()).isTrue();
		assertThat(form.getMethodAttribute()).matches("post");
		assertThat(form.getActionAttribute()).matches("/web/project/5");
		form.getInputByName("name").setValueAttribute("New project name");
		
		HtmlButton button = form.getButtonByName("submit-button");
		assertThat(button.getTextContent()).matches(env.getProperty("confirm"));
		button.click();
		
		verify(updateProjectController).updateProjectWebEndPoint(
				isA(Authentication.class), 
				eq(5L), 
				eq(new ProjectDataWebDto("New project name")), 
				isA(BindingResult.class));
	}
	
	@Test
	@WithMockUser
	void test_deleteProject() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(Collections.emptyList());
		when(deleteProjectController.deleteProjectWebEndpoint(any(), anyLong()))
			.thenReturn(TEST_VIEW_PAGE);
		
		HtmlPage page = webClient.getPage("/web/project/5/tasks");
		
		page.getHtmlElementById("activeProject-menu-trigger").mouseOver();
		HtmlElement triggerLink = page.getHtmlElementById("activeProject-delete-trigger");
		assertThat(triggerLink.getTextContent()).matches(env.getProperty("delete.project"));
		HtmlForm form = page.getFormByName("activeProject-delete-form");
		assertThat(form.getActionAttribute()).matches("/web/project/5");

		triggerLink.click();
		form.getButtonByName("submit-button").click();
		
		verify(deleteProjectController).deleteProjectWebEndpoint(isA(Authentication.class), eq(5L));
	}
	
	@Test
	@WithMockUser
	void test_deleteProject_confirmationBox_visibility() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(Collections.emptyList());
		
		HtmlPage page = webClient.getPage("/web/project/5/tasks");
		
		page.getHtmlElementById("activeProject-menu-trigger").mouseOver();
		HtmlForm form = page.getFormByName("activeProject-delete-form");
		assertThat(form.isDisplayed()).isFalse();

		HtmlElement triggerLink = page.getHtmlElementById("activeProject-delete-trigger");
		triggerLink.click();
		assertThat(form.isDisplayed()).isTrue();
		
		form.getButtonByName("cancel-button").click();
		assertThat(form.isDisplayed()).isFalse();
		
		triggerLink.click();
		assertThat(form.isDisplayed()).isTrue();
		
		page.getElementById("activeProject-delete-close").click();
		assertThat(form.isDisplayed()).isFalse();
	}
	
	@Test
	@WithMockUser
	void test_deleteTask() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		List<Task> tasks = new LinkedList<>();
		tasks.add(new Task(4L, "task name", "task description", false, 3));
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(tasks);
		when(deleteTaskController.deleteTaskEndPoint(any(), anyLong(), anyLong()))
			.thenReturn(TEST_VIEW_PAGE);
		
		HtmlPage page = webClient.getPage("/web/project/5/tasks");
		page.getHtmlElementById("task-4-menu-trigger").mouseOver();
		HtmlElement linkTrigger = page.getHtmlElementById("task-4-delete-trigger");
		assertThat(linkTrigger.getTextContent()).matches(env.getProperty("delete.task"));
		linkTrigger.click();
		
		HtmlForm form = page.getFormByName("task-delete-form");
		assertThat(form.getActionAttribute()).matches("/web/project/5/task/4");
		form.getButtonByName("submit-button").click();

		verify(deleteTaskController).deleteTaskEndPoint(isA(Authentication.class), eq(5L), eq(4L));
	}
	
	@Test
	@WithMockUser
	void test_deleteTask_confirmationBox_visibility() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		List<Task> tasks = new LinkedList<>();
		tasks.add(new Task(6L, "task name", "task description", false, 3));
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(tasks);
		
		HtmlPage page = webClient.getPage("/web/project/5/tasks");
		
		page.getHtmlElementById("task-6-menu-trigger").mouseOver();
		HtmlForm form = page.getFormByName("task-delete-form");
		assertThat(form.isDisplayed()).isFalse();
		
		HtmlElement linkTrigger = page.getHtmlElementById("task-6-delete-trigger");
		linkTrigger.click();
		assertThat(form.isDisplayed()).isTrue();

		form.getButtonByName("cancel-button").click();
		assertThat(form.isDisplayed()).isFalse();
		
		linkTrigger.click();
		assertThat(form.isDisplayed()).isTrue();
		
		page.getElementById("task-delete-close").click();
		assertThat(form.isDisplayed()).isFalse();
	}
	
	@Test
	@WithMockUser
	void test_editTask_formVisibility() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		List<Task> tasks = new LinkedList<>();
		tasks.add(new Task(11L, "task name", "task description", false, 3));
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(tasks);
		
		HtmlPage page = webClient.getPage("/web/project/7/tasks");
		
		page.getHtmlElementById("task-11-menu-trigger").mouseOver();
		HtmlElement taskDescription = page.getHtmlElementById("task-11-description-box");
		HtmlForm form = page.getFormByName("task-11-edit-form");
		assertThat(taskDescription.isDisplayed()).isTrue();
		assertThat(form.isDisplayed()).isFalse();
		
		HtmlElement linkTrigger = page.getHtmlElementById("task-11-edit-trigger");
		linkTrigger.click();
		assertThat(taskDescription.isDisplayed()).isFalse();
		assertThat(form.isDisplayed()).isTrue();

		form.getButtonByName("cancel-button").click();
		assertThat(taskDescription.isDisplayed()).isTrue();
		assertThat(form.isDisplayed()).isFalse();
		
	}
	
	@Test
	@WithMockUser
	void test_editTask_formFunctionality() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		List<Task> tasks = new LinkedList<>();
		tasks.add(new Task(11L, "task test name", "task test description", false, 3));
		when(loadTasks.load(isA(ProjectIdDTO.class))).thenReturn(tasks);
		when(updateTaskController.updateTaskEndPoint(any(), anyLong(), anyLong(), any(), any()))
			.thenReturn(TEST_VIEW_PAGE);
		
		HtmlPage page = webClient.getPage("/web/project/7/tasks");
		
		page.getHtmlElementById("task-11-menu-trigger").mouseOver();
		page.getHtmlElementById("task-11-edit-trigger").click();
		
		HtmlForm form = page.getFormByName("task-11-edit-form");
		assertThat(form.getActionAttribute()).matches("/web/project/7/task/11");
		
		HtmlInput nameInput = form.getInputByName("name");
		assertThat(nameInput.getValueAttribute()).matches("task test name");
		
		HtmlTextArea descriptionInput = form.getTextAreaByName("description");
		assertThat(descriptionInput.getTextContent()).matches("task test description");
		
		nameInput.setValueAttribute("new task name");
		descriptionInput.setText("task new description");
		form.getButtonByName("submit-button").click();
		
		verify(updateTaskController).updateTaskEndPoint(isA(Authentication.class), 
				eq(7L), eq(11L), eq(new TaskDataWebDto("new task name", "task new description"))
				, isA(BindingResult.class));
	}
}
