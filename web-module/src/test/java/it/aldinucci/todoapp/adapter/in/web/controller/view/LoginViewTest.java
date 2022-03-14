package it.aldinucci.todoapp.adapter.in.web.controller.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import it.aldinucci.todoapp.adapter.in.web.controller.LoginWebController;
import it.aldinucci.todoapp.adapter.in.web.controller.RegisterUserWebController;
import it.aldinucci.todoapp.adapter.in.web.dto.RegisterUserDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {LoginWebController.class})
class LoginViewTest {

	@Autowired
	private WebClient webClient;
	
	@MockBean
	private RegisterUserWebController registerController;
	
	private HtmlPage page;
	
	@BeforeEach
	void setUp() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(registerController.showRegistrationPage(any())).thenReturn("test-view-page");
		page = webClient.getPage("/login");
	}
	
	@Test
	void test_registerLink() {
		assertThatCode(() -> page.getAnchorByHref("/user/register").click())
			.doesNotThrowAnyException();
		
		verify(registerController).showRegistrationPage(new RegisterUserDto());
	}
	
	@Test
	void test_login() throws ElementNotFoundException, IOException {
		HtmlForm form = page.getFormByName("login-form");
		
	 	assertThatCode(() -> form.getInputByName("username").setValueAttribute("test@email.it"))
	 		.doesNotThrowAnyException();
		
	 	assertThatCode(() -> form.getInputByName("password").setValueAttribute("password"))
	 		.doesNotThrowAnyException();
		
	 	assertThatCode(() -> form.getButtonByName("log-in").click())
	 		.doesNotThrowAnyException();
		
		assertThat(form.getActionAttribute()).matches("/login");
		assertThat(form.getMethodAttribute()).matches("post");
	}
	
}
