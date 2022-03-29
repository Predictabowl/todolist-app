package it.aldinucci.todoapp.configuration.integration.login;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.ResetPasswordTokenJPARepository;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class PasswordResetIT {
	
	@Autowired
	private UserJPARepository userRepo;
	
	@Autowired
	private ResetPasswordTokenJPARepository tokenRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@LocalServerPort
	private int port; 
	
	private WebDriver webDriver;
	
	private GreenMail mailServer;
	
	private UserJPA user;
	
	private String baseUrl;
	
	@BeforeEach
	void setUp() {
		databaseSetup();
		mailServer = new GreenMail(ServerSetupTest.SMTP)
				.withConfiguration(GreenMailConfiguration.aConfig()
						.withUser("todolist@email.it", "mailPassword"));
		mailServer.start();
		webDriver = new HtmlUnitDriver();
		baseUrl = "http://localhost:"+port;
	}
	
	@AfterEach
	void tearDown() {
		webDriver.quit();
		mailServer.stop();
	}
	
	@Test
	void test_resetPassword() throws IOException, MessagingException {
		webDriver.get(baseUrl+"/user/register/password/reset");
		webDriver.findElement(By.name("email")).sendKeys("test@email.it");
		webDriver.findElement(By.name("submit-button")).click();
		
		List<ResetPasswordTokenJPA> tokens = tokenRepo.findAll();
		assertThat(tokens).hasSize(1);
		ResetPasswordTokenJPA token = tokenRepo.findByUserEmail("test@email.it").get();
		assertThat(token.getToken()).isEqualTo(tokens.get(0).getToken());
		
		MimeMessage[] receivedMessages = mailServer.getReceivedMessages();
		assertThat(receivedMessages).hasSize(1);
		String link = recoverLink(receivedMessages[0].getContent().toString());
		
		webDriver.get(link);
		webDriver.findElement(By.name("password")).sendKeys("new password");
		webDriver.findElement(By.name("confirmedPassword")).sendKeys("new password");
		webDriver.findElement(By.name("submit-button")).click();
		
		assertThat(tokenRepo.findAll()).isEmpty();
		UserJPA updatedUser= userRepo.findById(user.getId()).get();
		assertThat(encoder.matches("new password", updatedUser.getPassword())).isTrue();
	}
	
	@Test
	void test_resetPassword_whenTokenExpired() throws IOException, MessagingException {
		webDriver.get(baseUrl+"/user/register/password/reset");
		webDriver.findElement(By.name("email")).sendKeys("test@email.it");
		webDriver.findElement(By.name("submit-button")).click();
		
		
		MimeMessage[] receivedMessages = mailServer.getReceivedMessages();
		assertThat(receivedMessages).hasSize(1);
		String link = recoverLink(receivedMessages[0].getContent().toString());
		
		expireToken();
		webDriver.get(link);
		
		assertThat(webDriver.getCurrentUrl()).matches("http://localhost:"+port+"/login");
	}
	
	@Test
	void test_resetPassword_whenTokenExpireDuringReset() throws IOException, MessagingException {
		webDriver.get(baseUrl+"/user/register/password/reset");
		webDriver.findElement(By.name("email")).sendKeys("test@email.it");
		webDriver.findElement(By.name("submit-button")).click();
		
		
		MimeMessage[] receivedMessages = mailServer.getReceivedMessages();
		assertThat(receivedMessages).hasSize(1);
		String link = recoverLink(receivedMessages[0].getContent().toString());
		
		webDriver.get(link);
		expireToken();
		
		webDriver.findElement(By.name("password")).sendKeys("new password");
		webDriver.findElement(By.name("confirmedPassword")).sendKeys("new password");
		webDriver.findElement(By.name("submit-button")).click();
		
		assertThat(tokenRepo.findAll()).isEmpty();
		UserJPA updatedUser= userRepo.findById(user.getId()).get();
		assertThat(encoder.matches("old password", updatedUser.getPassword())).isTrue();
	}
	
	
	private void databaseSetup() {
		tokenRepo.deleteAll();
		userRepo.deleteAll();
		user = userRepo.saveAndFlush(new UserJPA(
				null, "test@email.it", "username", encoder.encode("old password"), true));
	}
	
	private String recoverLink(String messageContent) {
		int start = messageContent.indexOf("href='")+6;
		int end = messageContent.indexOf("'>");
		return messageContent.substring(start, end);
	}
	
	private void expireToken() {
		ResetPasswordTokenJPA token = tokenRepo.findByUserEmail("test@email.it").get();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -1);
		token.setExpiryDate(calendar.getTime());
		tokenRepo.saveAndFlush(token);
	}
}
