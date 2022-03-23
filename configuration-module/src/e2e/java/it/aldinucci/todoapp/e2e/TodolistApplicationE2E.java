package it.aldinucci.todoapp.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
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
	void test_register_andLogin() throws IOException, MessagingException, InterruptedException {
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
	void test_resetPassword() throws IOException, MessagingException, InterruptedException {
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
	void test_createAndEditProjects() throws InterruptedException {
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
	void test_createAndEditTask() throws InterruptedException {
		fail("Test not implemented Yet!");
		
	}
	private String recoverLink(MimeMessage message) throws IOException, MessagingException {
		String messageContent = message.getContent().toString();
		int start = messageContent.indexOf("href='") + 6;
		int end = messageContent.indexOf("'>");
		return messageContent.substring(start, end);
	}

//	private void clearDatabase() {
//		entityManager.getTransaction().begin();
//		List<VerificationToken> vTokens = entityManager.createQuery("from VerificationToken", VerificationToken.class)
//				.getResultList();
//		vTokens.stream().forEach(t -> entityManager.remove(vTokens));
//		List<ResetPasswordToken> rpTokens = entityManager
//				.createQuery("from ResetPasswordToken", ResetPasswordToken.class).getResultList();
//		rpTokens.stream().forEach(t -> entityManager.remove(t));
//		List<UserJPA> users = entityManager.createQuery("from UserJPA", UserJPA.class).getResultList();
//		users.stream().forEach(u -> entityManager.remove(u));
//		entityManager.getTransaction().commit();
//	}
}
