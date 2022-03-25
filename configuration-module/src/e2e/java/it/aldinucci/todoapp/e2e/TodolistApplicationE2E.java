package it.aldinucci.todoapp.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.io.IOException;
import java.time.Duration;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * If running these tests outside of the maven build, as in the IDE,
 * then the application must be running beforehand, and it should be
 * manually relaunched after every run to reset its state
 * 
 * @author piero
 *
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TodolistApplicationE2E {

	private static final String OLD_PASSWORD = "old password";
	private static final String NEW_PASSWORD = "new password";

	private static final String USER_EMAIL = "test@email.it";

	private static int port = Integer.parseInt(System.getProperty("e2e.server.port", "8080"));

	private static String baseUrl;

	private static WebDriver webDriver;

	private static GreenMail mailServer;

	@BeforeAll
	static void setUpClass() {
		WebDriverManager.chromedriver().setup();
		mailServer = new GreenMail(ServerSetupTest.SMTP)
				.withConfiguration(GreenMailConfiguration.aConfig().withUser("todolist@email.it", "mailPassword"));
		mailServer.start();
		baseUrl = "http://localhost:" + port;
		webDriver = new ChromeDriver();
	}

	@AfterAll
	static void tearDownClass() {
		webDriver.quit();
		mailServer.stop();
	}

	@Test
	@Order(1)
	void test_register_andLogin() throws IOException, MessagingException {
		webDriver.get(baseUrl);

		webDriver.findElement(By.cssSelector("a[href*='/user/register'")).click();
		webDriver.findElement(By.name("email")).sendKeys(USER_EMAIL);
		webDriver.findElement(By.name("username")).sendKeys("Test User");
		webDriver.findElement(By.name("password")).sendKeys(OLD_PASSWORD);
		webDriver.findElement(By.name("confirmedPassword")).sendKeys(OLD_PASSWORD);
		webDriver.findElement(By.name("submit-button")).click();

		MimeMessage[] messages = mailServer.getReceivedMessages();
		assertThat(messages).hasSize(1);
		String link = recoverLink(messages[0]);

		webDriver.get(link);
		webDriver.findElement(By.cssSelector("a[href*='/login'")).click();
		webDriver.findElement(By.name("username")).sendKeys(USER_EMAIL);
		webDriver.findElement(By.name("password")).sendKeys(OLD_PASSWORD);
		webDriver.findElement(By.name("submit-button")).click();
		
		assertThat(webDriver.getCurrentUrl()).endsWith("/web");
	}
	
	@Test
	@Order(2)
	void test_resetPassword() throws IOException, MessagingException {
		webDriver.get(baseUrl+"/login");

		webDriver.findElement(By.cssSelector("a[href*='/user/register/password/reset")).click();
		webDriver.findElement(By.name("email")).sendKeys(USER_EMAIL);
		webDriver.findElement(By.name("submit-button")).click();
		
		MimeMessage[] messages = mailServer.getReceivedMessages();
		assertThat(messages).hasSize(2);
		String link = recoverLink(messages[1]);
		
		webDriver.get(link);
		webDriver.findElement(By.name("password")).sendKeys(NEW_PASSWORD);
		webDriver.findElement(By.name("confirmedPassword")).sendKeys(NEW_PASSWORD);
		webDriver.findElement(By.name("submit-button")).click();
		
		webDriver.findElement(By.cssSelector("a[href*='/login'")).click();
		webDriver.findElement(By.name("username")).sendKeys(USER_EMAIL);
		webDriver.findElement(By.name("password")).sendKeys(NEW_PASSWORD);
		webDriver.findElement(By.name("submit-button")).click();
		
		assertThat(webDriver.getCurrentUrl()).endsWith("/web");
	}
	
	@Test
	@Order(3)
	void test_createAndEditProjects() {
		webDriver.get(baseUrl+"/web");
		
		webDriver.findElement(By.id("open-new-project-card")).click();
		
		await().atMost(Duration.ofSeconds(3)).ignoreExceptions()
			.untilAsserted(() ->{  
				webDriver.findElement(By.name("new-project-form"))
					.findElement(By.name("name")).sendKeys("Forst Project");
			});
		
		webDriver.findElement(By.name("submit-button")).click();
		
		assertThat(webDriver.findElement(By.id("project-list")).getText())
			.contains("Forst Project");
		
		Actions actions = new Actions(webDriver);
		actions.moveToElement(webDriver.findElement(By.id("activeProject-menu-trigger")))
			.perform();
		webDriver.findElement(By.id("activeProject-edit-trigger")).click();
		
		WebElement editProjectForm = webDriver.findElement(By.name("activeProject-edit-form"));
		await().atMost(Duration.ofSeconds(3)).ignoreExceptions()
			.untilAsserted(() -> editProjectForm.findElement(By.name("name")).clear());
		editProjectForm.findElement(By.name("name")).sendKeys("First Project");
		editProjectForm.findElement(By.name("submit-button")).click();
		
		assertThat(webDriver.findElement(By.id("project-list")).getText())
		.contains("First Project");
	}

	@Test
	@Order(4)
	void test_createAndEditTask() {
		webDriver.get(baseUrl+"/web");
		webDriver.findElements(By.cssSelector("a[href*='/web/project")).stream()
			.filter(e -> e.getText().contains("First Project")).findFirst().get().click();
		
		webDriver.findElement(By.id("add-task-link")).click();
		WebElement newTaskForm = webDriver.findElement(By.name("new-task-form"));
		newTaskForm.findElement(By.name("name")).sendKeys("New Task");
		newTaskForm.findElement(By.name("description")).sendKeys("This is a task description");
		newTaskForm.findElement(By.name("submit-button")).click();
		
		WebElement taskList = webDriver.findElement(By.id("tasks-list"));
		assertThat(taskList.getText())
			.contains("New Task")
			.contains("This is a task description");
		
		Actions actions = new Actions(webDriver);
		actions.moveToElement(taskList.findElement(By.className("dropdown-trigger")))
			.perform();
		taskList.findElement(By.partialLinkText("Edit Task")).click();
		
		WebElement editTaskForm = taskList.findElements(By.cssSelector("form")).stream().filter(e 
				-> e.getAttribute("name").endsWith("-edit-form")).findFirst().get();
		WebElement nameInput = editTaskForm.findElement(By.name("name"));
		nameInput.clear();
		nameInput.sendKeys("Old Task");
		editTaskForm.findElement(By.name("description")).sendKeys(" (edited)");
		editTaskForm.findElement(By.name("submit-button")).click();
		
		assertThat(webDriver.findElement(By.id("tasks-list")).getText())
			.contains("Old Task")
			.contains("This is a task description (edited)");
	}
	
	@Test
	@Order(5)
	void test_toggleTaskStatus(){
		WebElement taskList = webDriver.findElement(By.id("tasks-list"));
		
		WebElement toggleForm = taskList.findElements(By.tagName("form")).stream().filter(e ->
				e.getAttribute("name").startsWith("complete-task-")).findFirst().get();
		toggleForm.findElement(By.name("submit-button")).click();
		
		 assertThat(webDriver.findElement(By.id("tasks-list")).
				 findElements(By.cssSelector("li"))).isEmpty();
		 
		Actions actions = new Actions(webDriver);
		actions.moveToElement(webDriver.findElement(By.id("activeProject-menu-trigger")))
			.perform();
		webDriver.findElement(By.id("toggle-completed-tasks")).click();
		
		WebElement completedTaskList = webDriver.findElement(By.id("completed-tasks-list"));
		await().atMost(Duration.ofSeconds(3)).ignoreExceptions().untilAsserted(() -> 
			assertThat(completedTaskList.findElements(By.cssSelector("li")).get(0).getText())
				.contains("Old Task")
				.contains("This is a task description (edited)"));
		
		toggleForm = completedTaskList.findElements(By.tagName("form")).stream().filter(e ->
			e.getAttribute("name").startsWith("complete-task-")).findFirst().get();
		toggleForm.findElement(By.name("submit-button")).click();
		
		
		assertThat(webDriver.findElement(By.id("completed-tasks-list"))
				.findElements(By.cssSelector("li"))).isEmpty();

		assertThat(webDriver.findElement(By.id("tasks-list"))
				.findElement(By.cssSelector("li")).getText())
			.contains("Old Task")
			.contains("This is a task description (edited)");
	}
	
	@Test
	@Order(6)
	void test_deleteTask(){
		WebElement taskList = webDriver.findElement(By.id("tasks-list"));
		Actions actions = new Actions(webDriver);
		actions.moveToElement(taskList.findElement(By.className("dropdown-trigger")))
			.perform();
		taskList.findElement(By.partialLinkText("Delete Task")).click();
		
		webDriver.findElement(By.id("task-delete-form"))
			.findElement(By.name("submit-button")).click();
		
		
		assertThat(webDriver.findElement(By.id("tasks-list"))
				.findElements(By.cssSelector("li"))).isEmpty();
		assertThat(webDriver.findElement(By.id("completed-tasks-list"))
				.findElements(By.cssSelector("li"))).isEmpty();
	}
	
	@Test
	@Order(7)
	void test_deleteProject() {
		Actions actions = new Actions(webDriver);
		actions.moveToElement(webDriver.findElement(By.id("activeProject-menu-trigger")))
			.perform();
		webDriver.findElement(By.id("activeProject-delete-trigger")).click();
		
		await().atMost(Duration.ofSeconds(3)).ignoreExceptions().untilAsserted(() 
			-> webDriver.findElement(By.id("activeProject-delete-form"))
				.findElement(By.name("submit-button")).click());
		
		assertThat(webDriver.getCurrentUrl()).endsWith(baseUrl+"/web");
		assertThat(webDriver.findElement(By.id("project-list"))
				.findElements(By.cssSelector("li"))).isEmpty();
	}
	
	@Test
	@Order(8)
	void test_logOut() {
		Actions actions = new Actions(webDriver);
		actions.moveToElement(webDriver.findElement(By.id("user-menu")))
			.perform();
		webDriver.findElement(By.id("logout-link")).click();
		
		assertThat(webDriver.getCurrentUrl().trim()).endsWith(baseUrl+"/login?logout");
		webDriver.get(baseUrl+"/web");
		assertThat(webDriver.getCurrentUrl().trim()).matches(baseUrl+"/login");
	}
	
	private String recoverLink(MimeMessage message) throws IOException, MessagingException {
		String messageContent = message.getContent().toString();
		return messageContent.substring(
				messageContent.indexOf("href='") + 6,
				messageContent.indexOf("'>"));
	}
}
