package it.aldinucci.todoapp.adapter.in.web.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import it.aldinucci.todoapp.adapter.in.web.controller.RegisterUserWebController;
import it.aldinucci.todoapp.application.port.in.CreateUserUsePort;
import it.aldinucci.todoapp.application.port.in.SendVerificationEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.EmailLinkDTO;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDtoOut;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppEmailAlreadyRegisteredException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.RegisterUserDto;

/**
 * These test class should only check if the view will interact correctly with the
 * controller, is not his responsibility to test the controller correct behavior.
 *  
 * @author piero
 *
 */

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {RegisterUserWebController.class})
@PropertySource("classpath:messages.properties")
class RegisterUserWebViewTest {
	
	@Autowired
	private WebClient webClient;
	
	@MockBean
	private CreateUserUsePort createUser;
	
	@MockBean
	private AppGenericMapper<RegisterUserDto, NewUserDTOIn> mapper;
	
	@MockBean
	private SendVerificationEmailUsePort sendMail;
	
	@SpyBean
	private RegisterUserWebController controller;
	
	@Autowired
	private Environment env;
	
	private HtmlPage page;
	
	@BeforeEach
	void setUp() throws FailingHttpStatusCodeException, MalformedURLException, IOException, AppEmailAlreadyRegisteredException {
		when(mapper.map(isA(RegisterUserDto.class))).thenReturn(new NewUserDTOIn("name", "user@email.it", "test pass"));
		when(createUser.create(isA(NewUserDTOIn.class))).thenReturn(
				new NewUserDtoOut(
						new User("user@email.it",null,null),
						new VerificationToken()));
		doNothing().when(sendMail).send(isA(EmailLinkDTO.class));
		
		page = webClient.getPage("/user/register");
	}
	
	
	@Test
	void test_registrationForm_filledSuccess() throws ElementNotFoundException, IOException{
		HtmlForm form = page.getFormByName("user-register");
		form.getInputByName("email").setValueAttribute("user@email.it");
		form.getInputByName("username").setValueAttribute("name");
		form.getInputByName("password").setValueAttribute("test pass");
		form.getInputByName("confirmedPassword").setValueAttribute("test pass");
		form.getButtonByName("submit-button").click();
		
		RegisterUserDto userDto = new RegisterUserDto("user@email.it", "name", "test pass", "test pass");
		ArgumentCaptor<BindingResult> bindingResult = ArgumentCaptor.forClass(BindingResult.class);
		verify(controller).postRegistrationPage(eq(userDto), bindingResult.capture());
		assertThat(bindingResult.getValue().getAllErrors()).isEmpty();
	}
	
	@Test
	void test_registrationForm_ValidationErrors() throws ElementNotFoundException, IOException{
		HtmlForm form = page.getFormByName("user-register");
		HtmlInput emailInput = form.getInputByName("email");
		emailInput.setValueAttribute("useremail.it");
		form.getInputByName("username").setValueAttribute("name");
		form.getInputByName("password").setValueAttribute("pass1");
		form.getInputByName("confirmedPassword").setValueAttribute("pass2");
		form.getButtonByName("submit-button").click();
		
		RegisterUserDto userDto = new RegisterUserDto("useremail.it", "name", "pass1", "pass2");
		ArgumentCaptor<BindingResult> bindingResult = ArgumentCaptor.forClass(BindingResult.class);
		verify(controller).postRegistrationPage(eq(userDto), bindingResult.capture());
		assertThat(bindingResult.getValue().getAllErrors()).hasSize(2);
	}
	
	@Test
	void test_registrationForm_links(){
		assertThat(page.getAnchorByHref("/user/register/resend/verification").getTextContent())
			.matches(env.getProperty("send.email.verification.again"));
		
		assertThat(page.getAnchorByHref("/login").getTextContent())
			.matches(env.getProperty("back.to.login"));
	}
	
}
