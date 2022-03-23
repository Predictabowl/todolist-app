package it.aldinucci.todoapp.adapter.in.web.controller.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import it.aldinucci.todoapp.adapter.in.web.controller.RequestResetPasswordWebController;
import it.aldinucci.todoapp.application.port.in.GetOrCreatePasswordResetTokenUsePort;
import it.aldinucci.todoapp.application.port.in.SendResetPasswordEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.EmailLinkDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.ResetPasswordToken;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {RequestResetPasswordWebController.class})
@PropertySource("classpath:messages.properties")
@AutoConfigureMockMvc(addFilters = false) //no need of security, is responsibility of the controllers tests 
class RequestResetPasswordWebViewTest {

	private static final String BASE_URL = "/user/register/password/reset";
	
	@Autowired
	private WebClient webClient;
	
	@MockBean
	private GetOrCreatePasswordResetTokenUsePort retrieveToken;
	
	@MockBean
	private SendResetPasswordEmailUsePort sendEmail;
	
	@SpyBean
	private RequestResetPasswordWebController sutController;
	
	@Test
	void test_getView_withEmptyEmail_shouldNotPost() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = webClient.getPage(BASE_URL);
		
		HtmlForm form = page.getFormByName("email-form");
		assertThat(form.getActionAttribute()).matches(BASE_URL);
		HtmlButton submitButton = form.getButtonByName("submit-button");
		assertThat(submitButton.getTextContent()).matches("Send");
		submitButton.click();
		
		verify(sutController,times(0)).tokenCreation(any(), any());
	}
	
	@Test
	void test_malformedEmail_shouldNotPost() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		HtmlPage page = webClient.getPage(BASE_URL);
		
		HtmlForm form = page.getFormByName("email-form");
		form.getInputByName("email").type("invalid.email");
		form.getButtonByName("submit-button").click();
		
		verifyNoInteractions(retrieveToken);
		verifyNoInteractions(sendEmail);
	}
	
	@Test
	void test_get_emailField() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(retrieveToken.get(any())).thenReturn(Optional.empty());
		
		HtmlPage page = webClient.getPage(BASE_URL);
		
		HtmlForm form = page.getFormByName("email-form");
		form.getInputByName("email").type("some@email.it");
		form.getButtonByName("submit-button").click();

		verify(retrieveToken).get(new UserIdDTO("some@email.it"));
		verifyNoInteractions(sendEmail);
	}
	
	@Test
	void test_post_whenEmailNotFound() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(retrieveToken.get(any())).thenReturn(Optional.empty());

		List<NameValuePair> params = Arrays.asList(new NameValuePair("email", "test@email.it"));
		WebRequest webRequest = new WebRequest(new URL("http://localhost"+BASE_URL), HttpMethod.POST);
		webRequest.setRequestParameters(params);
		HtmlPage page = webClient.getPage(webRequest);
		
		HtmlForm form = page.getFormByName("email-form");
		
		assertThat(form.getActionAttribute()).matches(BASE_URL);
		assertThat(form.getTextContent()).contains(
				"This email is not registered");
		verify(retrieveToken).get(new UserIdDTO("test@email.it"));
		verifyNoInteractions(sendEmail);
	}
	
	@Test
	void test_post_whenTokenIsFound() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		ResetPasswordToken token = new ResetPasswordToken("code", Calendar.getInstance().getTime(), "some@email.it");
		when(retrieveToken.get(any())).thenReturn(Optional.of(token));
		
		List<NameValuePair> params = Arrays.asList(new NameValuePair("email", "test@email.it"));
		WebRequest webRequest = new WebRequest(new URL("http://localhost"+BASE_URL), HttpMethod.POST);
		webRequest.setRequestParameters(params);
		HtmlPage page = webClient.getPage(webRequest);
		
		verify(retrieveToken).get(new UserIdDTO("test@email.it"));
		//no need to fully test it, the controller tests should take care of it
		verify(sendEmail).send(isA(EmailLinkDTO.class)); 
		
		page.getElementById("content");
		assertThat(page.getElementById("content").getTextContent())
			.contains("An email was sent to the address")
			.contains("test@email.it");
	}
	
}
