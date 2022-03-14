package it.aldinucci.todoapp.adapter.in.web.controller.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import it.aldinucci.todoapp.adapter.in.web.controller.LoginWebController;
import it.aldinucci.todoapp.adapter.in.web.controller.RegisterUserWebController;
import it.aldinucci.todoapp.adapter.in.web.dto.RegisterUserDto;
import it.aldinucci.todoapp.adapter.in.web.validator.RegisterUserValidator;
import it.aldinucci.todoapp.application.port.in.CreateUserUsePort;
import it.aldinucci.todoapp.application.port.in.SendVerificationEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDtoOut;
import it.aldinucci.todoapp.application.port.in.dto.VerificationLinkDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppEmailAlreadyRegisteredException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

/**
 * These test class should only check if the view will interact correctly with the
 * controller, is not his responsibility to test the controller correct behavior.
 *  
 * @author piero
 *
 */

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {RegisterUserWebController.class})
@AutoConfigureMockMvc(addFilters = false)
@PropertySource("classpath:messages.properties")
class RegisterSentNotificationViewTest {
	
	private static final String FIXTURE_EMAIL = "test@email.it";

	@Autowired
	private WebClient webClient;
	
	@MockBean
	private CreateUserUsePort createUser;
	
	@MockBean
	private AppGenericMapper<RegisterUserDto, NewUserDTOIn> mapper;
	
	@MockBean
	private RegisterUserValidator userValidator;
	
	@MockBean
	private SendVerificationEmailUsePort sendMail;
	
	@MockBean
	private LoginWebController loginWebController;

	@Autowired
	private Environment env;
	
	private HtmlPage page;
	
	@BeforeEach
	void setUp() {
		when(userValidator.supports(RegisterUserDto.class)).thenReturn(true);
		doNothing().when(userValidator).validate(any(), any());
		when(mapper.map(isA(RegisterUserDto.class))).thenReturn(new NewUserDTOIn("name", FIXTURE_EMAIL, "test"));
		doNothing().when(sendMail).send(isA(VerificationLinkDTO.class));
		LocaleContextHolder.setLocale(Locale.ENGLISH);
	}
	
	
	@Test
	void test_notification() throws AppEmailAlreadyRegisteredException, FailingHttpStatusCodeException, IOException{
		when(createUser.create(isA(NewUserDTOIn.class))).thenReturn(
				new NewUserDtoOut(
						new User(FIXTURE_EMAIL,null,null),
						new VerificationToken()));
		
		WebRequest webRequest = new WebRequest(new URL("http://localhost:8080/user/register"), HttpMethod.POST);
		page = webClient.getPage(webRequest);

		String textContent = page.getBody().getTextContent();
		assertThat(textContent)
			.contains(env.getProperty("verification.mail.was.sent"))
			.contains(FIXTURE_EMAIL);
	}


}
