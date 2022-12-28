package it.aldinucci.todoapp.configuration.integration.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;
import it.aldinucci.todoapp.util.AppPasswordEncoder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class WebUpdateUserViewIT {

	@Autowired
	private UserJPARepository userRepo;
	
	@Autowired
	private AppPasswordEncoder encoder;
	
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
	void test_changeUsername() {
		webDriver.get(baseUrl+"/web/user/data");
		WebElement form = webDriver.findElement(By.cssSelector("form"));
		WebElement usernameInput = form.findElement(By.name("username"));
		usernameInput.clear();
		usernameInput.sendKeys("New username");
		form.findElement(By.name("submit-button")).click();
		
		UserJPA userJPA = userRepo.findByEmail(user.getEmail()).get();
		assertThat(userJPA.getUsername()).matches("New username");
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
		webDriver.findElement(By.name("submit-button")).click();
	}
}
