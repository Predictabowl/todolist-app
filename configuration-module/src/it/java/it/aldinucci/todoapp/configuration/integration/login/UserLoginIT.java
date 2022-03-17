package it.aldinucci.todoapp.configuration.integration.login;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collections;

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

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.repository.UserJPARepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class UserLoginIT {

	@Autowired
	private UserJPARepository userRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@LocalServerPort
	int appPort;
	
	private String baseUrl;
	
	private WebDriver webDriver;
	
	@BeforeEach
	void setUp() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		userRepo.deleteAll();
		userRepo.save(new UserJPA(null, "user@email.it", "User test", encoder.encode("test2Pass"), true,
				Collections.emptyList()));
		userRepo.flush();
		webDriver = new HtmlUnitDriver();
		baseUrl = "http://localhost:"+appPort;
	}
	
	@Test
	void test_login() {
		webDriver.get(baseUrl);
		
		webDriver.findElement(By.name("username")).sendKeys("user@email.it");
		webDriver.findElement(By.name("password")).sendKeys("test2Pass");
		webDriver.findElement(By.name("log-in")).click();
		
		assertThat(webDriver.getCurrentUrl()).matches(baseUrl+"/web");
	}
	
}
