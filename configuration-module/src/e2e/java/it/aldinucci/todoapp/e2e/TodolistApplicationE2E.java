package it.aldinucci.todoapp.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.EntityManagerFactoryAccessor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.domain.VerificationToken;

//@SpringBootConfiguration
//@ExtendWith(SpringExtension.class)
class TodolistApplicationE2E {

	private static final String USER_PASSWORD = "password";

	private static final String USER_EMAIL = "test@email.it";

	private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));

	private String baseUrl;

	private WebDriver webDriver;

	private GreenMail mailServer;

//	@PersistenceContext
//	private EntityManager entityManager;

	@BeforeAll
	static void setUpClass() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	void setUp() {
//		clearDatabase();
		mailServer = new GreenMail(ServerSetupTest.SMTP)
				.withConfiguration(GreenMailConfiguration.aConfig().withUser("todolist@email.it", "mailPassword"));
		mailServer.start();
		baseUrl = "http://localhost:" + port;
		webDriver = new ChromeDriver();
	}

	@AfterEach
	void tearDown() {
		webDriver.quit();
		mailServer.stop();
	}

//	@Test
//	void test_login() {
//		webDriver.get(baseUrl);
//		
//		webDriver.findElement(By.name("username"));
//	}

	@Test
	void test_register() throws IOException, MessagingException, InterruptedException {
		webDriver.get(baseUrl);

		webDriver.findElement(By.cssSelector("a[href*='/user/register'")).click();
		webDriver.findElement(By.name("email")).sendKeys(USER_EMAIL);
		webDriver.findElement(By.name("username")).sendKeys("Test User");
		webDriver.findElement(By.name(USER_PASSWORD)).sendKeys(USER_PASSWORD);
		webDriver.findElement(By.name("confirmedPassword")).sendKeys(USER_PASSWORD);
		webDriver.findElement(By.name("submit-button")).click();

		MimeMessage[] messages = mailServer.getReceivedMessages();
		assertThat(messages).hasSize(1);
		String link = recoverLink(messages[0]);

		webDriver.get(link);
		webDriver.findElement(By.cssSelector("a[href*='/login'")).click();
		webDriver.findElement(By.name("username")).sendKeys(USER_EMAIL);
		webDriver.findElement(By.name(USER_PASSWORD)).sendKeys(USER_PASSWORD);
		webDriver.findElement(By.name("submit-button")).click();

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
