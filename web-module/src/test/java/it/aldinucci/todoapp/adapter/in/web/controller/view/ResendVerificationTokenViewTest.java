package it.aldinucci.todoapp.adapter.in.web.controller.view;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import it.aldinucci.todoapp.adapter.in.web.controller.LoginWebController;
import it.aldinucci.todoapp.adapter.in.web.controller.ResendVerificationTokenController;
import it.aldinucci.todoapp.adapter.in.web.dto.EmailWebDto;
import it.aldinucci.todoapp.application.port.in.RetrieveVerificationTokenUsePort;
import it.aldinucci.todoapp.application.port.in.SendVerificationEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.VerificationLinkDTO;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppEmailAlreadyRegisteredException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {ResendVerificationTokenController.class, LoginWebController.class})
class ResendVerificationTokenViewTest {
	
	@Autowired
	private WebClient webClient;
	
	@MockBean
	private RetrieveVerificationTokenUsePort retrieveToken;
	
	@MockBean
	private SendVerificationEmailUsePort sendMail;
	
	@SpyBean
	private ResendVerificationTokenController resendTokenController;
	
	private HtmlPage page;

	@BeforeEach
	void setUp() throws FailingHttpStatusCodeException, MalformedURLException, IOException, AppEmailAlreadyRegisteredException {
		when(retrieveToken.get(isA(UserIdDTO.class))).thenReturn(new VerificationToken());
		doNothing().when(sendMail).send(isA(VerificationLinkDTO.class));
		
		page = webClient.getPage("/user/register/resend/verification");
	}
	
	@Test
	void test_resendVerification() throws ElementNotFoundException, IOException {
		HtmlForm form = page.getFormByName("email-form");
		form.getInputByName("email").setValueAttribute("user@email.it");
		form.getButtonByName("submit-button").click();
		
		ArgumentCaptor<BindingResult> bindingResult = ArgumentCaptor.forClass(BindingResult.class);
		verify(resendTokenController)
			.resendVerificationToken(eq(new EmailWebDto("user@email.it")), bindingResult.capture());
		assertThat(bindingResult.getValue().getAllErrors()).isEmpty();
	}
	
	@Test
	void test_resendVerification_withEmailNotValid() throws ElementNotFoundException, IOException {
		HtmlForm form = page.getFormByName("email-form");
		form.getInputByName("email").setValueAttribute("Malformed");
		form.getButtonByName("submit-button").click();
		
		ArgumentCaptor<BindingResult> bindingResult = ArgumentCaptor.forClass(BindingResult.class);
		verify(resendTokenController)
			.resendVerificationToken(eq(new EmailWebDto("Malformed")), bindingResult.capture());
		assertThat(bindingResult.getValue().getAllErrors()).hasSize(1);
	}
}
