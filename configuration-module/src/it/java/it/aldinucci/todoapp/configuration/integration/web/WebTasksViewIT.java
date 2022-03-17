package it.aldinucci.todoapp.configuration.integration.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.TaskJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.TaskJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class WebTasksViewIT {

	@Autowired
	private UserJPARepository userRepo;
	
	@Autowired
	private ProjectJPARepository projectRepo;
	
	@Autowired
	private TaskJPARepository taskRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@LocalServerPort
	int appPort;
	
	private String baseUrl;
	
	private WebDriver webDriver;

	private ProjectJPA project;
	
	@BeforeEach
	void setUp() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		databaseSetup();
		webDriver = new HtmlUnitDriver();
		baseUrl = "http://localhost:"+appPort;
		doLogin();
	}

	@Test
	void test_createNewTasks() {
		webDriver.get(baseUrl+"/web/project/"+project.getId()+"/tasks");
		
		webDriver.findElement(By.id("add-task-link")).click();
		WebElement form = webDriver.findElement(By.name("new-task-form"));
		form.findElement(By.name("name")).sendKeys("First Task");
		form.findElement(By.name("description")).sendKeys("Description of first Task");
		form.findElement(By.name("submit-button")).click();
		
		webDriver.findElement(By.id("add-task-link")).click();
		form = webDriver.findElement(By.name("new-task-form"));
		form.findElement(By.name("name")).sendKeys("Second Task");
		form.findElement(By.name("description")).sendKeys("Description of second Task");
		form.findElement(By.name("submit-button")).click();
		
		
		List<TaskJPA> tasks = taskRepo.findAll();
		assertThat(tasks).hasSize(2);
		TaskJPA task1 = tasks.get(0);
		TaskJPA task2 = tasks.get(1);
		assertThat(task1.getName()).matches("First Task");
		assertThat(task1.getDescription()).matches("Description of first Task");
		assertThat(task1.getOrderInProject()).isZero();
		assertThat(task1.isCompleted()).isFalse();
		
		assertThat(task2.getName()).matches("Second Task");
		assertThat(task2.getDescription()).matches("Description of second Task");
		assertThat(task2.getOrderInProject()).isEqualTo(1);
		assertThat(task2.isCompleted()).isFalse();
		
	}
	
	@Test
	void test_viewTasks() {
		TaskJPA task1 = taskRepo.save(new TaskJPA("task 1", "descr 1", false, project));
		TaskJPA task2 = taskRepo.save(new TaskJPA("task 2", "descr 2", true, project));
		TaskJPA task3 = taskRepo.save(new TaskJPA("task 3", "descr 3", false, project));
		List<TaskJPA> tasks = new LinkedList<>();
		tasks.add(task1);
		tasks.add(task2);
		tasks.add(task3);
		project.setTasks(tasks);
		projectRepo.saveAndFlush(project);
		
		webDriver.get(baseUrl+"/web/project/"+project.getId()+"/tasks");
		
		WebElement taskList = webDriver.findElement(By.id("tasks-list"));
		assertThat(taskList.getText())
			.contains("task 1").contains("descr 1")
			.contains("task 3").contains("descr 3")
			.doesNotContain("task 2").doesNotContain("descr 2");
		
		WebElement hoverMenu = webDriver.findElement(By.id("activeProject-menu-trigger"));
		Actions action = new Actions(webDriver);
		action.moveToElement(hoverMenu).perform();
		webDriver.findElement(By.id("toggle-completed-tasks")).click();
		WebElement completedTaskList = webDriver.findElement(By.id("completed-tasks-list"));
		assertThat(completedTaskList.getText())
			.doesNotContain("task 1").doesNotContain("descr 1")
			.doesNotContain("task 3").doesNotContain("descr 3")
			.contains("task 2").contains("descr 2");
	}
	
	@Test
	void test_toggleTaskCompletedStatus() {
		TaskJPA task1 = taskRepo.save(new TaskJPA("task 1", "descr 1", false, project));
		List<TaskJPA> tasks = new LinkedList<>();
		tasks.add(task1);
		project.setTasks(tasks);
		projectRepo.saveAndFlush(project);
		
		webDriver.get(baseUrl+"/web/project/"+project.getId()+"/tasks");
		WebElement hoverMenu = webDriver.findElement(By.id("activeProject-menu-trigger"));
		Actions action = new Actions(webDriver);
		action.moveToElement(hoverMenu).perform();
		webDriver.findElement(By.id("toggle-completed-tasks")).click();
		
		WebElement toggleTaskForm = webDriver.findElement(By.name("complete-task-"+task1.getId()+"-form"));
		toggleTaskForm.findElement(By.name("submit-button")).click();
		
		assertThat(taskRepo.findById(task1.getId()).get().isCompleted()).isTrue();
		
		toggleTaskForm = webDriver.findElement(By.name("complete-task-"+task1.getId()+"-form"));
		toggleTaskForm.findElement(By.name("submit-button")).click();
		
		assertThat(taskRepo.findById(task1.getId()).get().isCompleted()).isFalse();
	}
	
	private void databaseSetup() {
		userRepo.deleteAll();
		List<ProjectJPA> projects =  new LinkedList<>();
		UserJPA user = userRepo.save(new UserJPA(null, "user@email.it", "User test", encoder.encode("test2Pass"), true,
				projects));
		project = projectRepo.saveAndFlush(new ProjectJPA("First Project", user));
		projects.add(project);
		user.setProjects(projects);
		userRepo.saveAndFlush(user);
	}
	
	private void doLogin() {
		webDriver.get(baseUrl);
		
		webDriver.findElement(By.name("username")).sendKeys("user@email.it");
		webDriver.findElement(By.name("password")).sendKeys("test2Pass");
		webDriver.findElement(By.name("log-in")).click();
	}
}
