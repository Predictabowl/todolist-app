package it.aldinucci.todoapp.configuration.integration.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByName;
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
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ProjectJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class WebProjectViewIT {

	@Autowired
	private UserJPARepository userRepo;
	
	@Autowired
	private ProjectJPARepository projectRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@LocalServerPort
	int appPort;
	
	private String baseUrl;
	
	private WebDriver webDriver;

	private UserJPA user;
	
	@BeforeEach
	void setUp() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		databaseSetup();
		webDriver = new HtmlUnitDriver();
		baseUrl = "http://localhost:"+appPort;
		doLogin();
	}
	
	@AfterEach
	void tearDown() {
		webDriver.quit();
	}

	@Test
	void test_createNewProjects() {
		webDriver.get(baseUrl+"/web");
		webDriver.findElement(ById.id("open-new-project-card")).click();
		webDriver.findElement(ByName.name("name")).sendKeys("New Test Project");
		webDriver.findElement(By.name("submit-button")).click();
		
		assertThat(webDriver.findElement(By.id("activeProject-title")).getText())
			.contains("New Test Project");		
		
		webDriver.findElement(ById.id("open-new-project-card")).click();
		webDriver.findElement(ByName.name("name")).sendKeys("Second Test Project");
		webDriver.findElement(By.name("submit-button")).click();
		
		assertThat(webDriver.findElement(By.id("activeProject-title")).getText())
			.contains("Second Test Project");
		
		List<ProjectJPA> projects = projectRepo.findAll();
		assertThat(projects).hasSize(2);
		ProjectJPA project1 = projects.get(0);
		ProjectJPA project2 = projects.get(1);
		assertThat(project1.getName()).matches("New Test Project");
		assertThat(project2.getName()).matches("Second Test Project");
		
		
		assertThat(webDriver.getCurrentUrl()).matches(baseUrl+"/web/project/"+project2.getId()+"/tasks");
	}
	
	@Test
	void test_viewProjects() {
		List<ProjectJPA> projects = new LinkedList<>();
		ProjectJPA project1 = projectRepo.save(new ProjectJPA("project 1", user));
		ProjectJPA project2 = projectRepo.save(new ProjectJPA("project 2", user));
		projects.add(project1);
		projects.add(project2);
		user.setProjects(projects);
		userRepo.saveAndFlush(user);
		
		webDriver.get(baseUrl+"/web");
		
		WebElement project1Link = webDriver.findElement(By.linkText("project 1"));
		assertThat(project1Link.getAttribute("href"))
			.matches(baseUrl+"/web/project/"+project1.getId()+"/tasks");
		assertThat(webDriver.findElement(By.linkText("project 2")).getAttribute("href"))
			.matches(baseUrl+"/web/project/"+project2.getId()+"/tasks");
		
		project1Link.click();
		assertThat(webDriver.findElement(By.id("activeProject-title")).getText())
			.contains("project 1");
	}
	
	@Test
	void test_deleteProject() {
		List<ProjectJPA> projects = new LinkedList<>();
		ProjectJPA project1 = projectRepo.save(new ProjectJPA("project 1", user));
		ProjectJPA project2 = projectRepo.save(new ProjectJPA("project 2", user));
		projects.add(project1);
		projects.add(project2);
		user.setProjects(projects);
		userRepo.saveAndFlush(user);
		
		webDriver.get(baseUrl+"/web/project/"+project2.getId()+"/tasks");
		
		Actions actions = new Actions(webDriver);
		WebElement menuTrigger = webDriver.findElement(By.id("activeProject-menu-trigger"));
		actions.moveToElement(menuTrigger).perform();
		webDriver.findElement(By.id("activeProject-delete-trigger")).click();
		webDriver.findElement(By.id("activeProject-delete-form"))
			.findElement(By.name("submit-button")).click();
		
		List<ProjectJPA> actualProjects = projectRepo.findAll();
		assertThat(actualProjects).containsExactly(project1);
	}
	
	@Test
	void test_editProject() {
		List<ProjectJPA> projects = new LinkedList<>();
		ProjectJPA project1 = projectRepo.save(new ProjectJPA("project 1", user));
		ProjectJPA project2 = projectRepo.save(new ProjectJPA("project 2", user));
		projects.add(project1);
		projects.add(project2);
		user.setProjects(projects);
		userRepo.saveAndFlush(user);
		
		webDriver.get(baseUrl+"/web/project/"+project1.getId()+"/tasks");
		
		Actions actions = new Actions(webDriver);
		WebElement menuTrigger = webDriver.findElement(By.id("activeProject-menu-trigger"));
		actions.moveToElement(menuTrigger).perform();
		webDriver.findElement(By.id("activeProject-edit-trigger")).click();
		WebElement editForm = webDriver.findElement(By.id("activeProject-edit-form"));
		
		WebElement nameInput = editForm.findElement(By.name("name"));
		assertThat(nameInput.getAttribute("value")).matches("project 1");
		nameInput.clear();
		nameInput.sendKeys("New project name");
		
		editForm.findElement(By.name("submit-button")).click();
		
		Optional<ProjectJPA> editedProject = projectRepo.findById(project1.getId());
		assertThat(editedProject).isPresent();
		assertThat(editedProject.get().getName()).matches("New project name");
	}
	
	
	private void databaseSetup() {
		userRepo.deleteAll();
		user = userRepo.saveAndFlush(new UserJPA(null, "user@email.it", "User test", encoder.encode("test2Pass"),
				true, Collections.emptyList()));
	}
	
	private void doLogin() {
		webDriver.get(baseUrl);
		
		webDriver.findElement(By.name("username")).sendKeys("user@email.it");
		webDriver.findElement(By.name("password")).sendKeys("test2Pass");
		webDriver.findElement(By.name("log-in")).click();
	}
}
