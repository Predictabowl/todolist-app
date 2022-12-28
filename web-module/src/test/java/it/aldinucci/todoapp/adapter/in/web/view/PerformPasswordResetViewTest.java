package it.aldinucci.todoapp.adapter.in.web.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.BindingResult;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

import it.aldinucci.todoapp.adapter.in.web.controller.PerformResetPasswordWebController;
import it.aldinucci.todoapp.application.port.in.ChangeUserPasswordUsePort;
import it.aldinucci.todoapp.application.port.in.VerifyResetPasswordTokenUsePort;
import it.aldinucci.todoapp.webcommons.dto.InputPasswordsDto;

@WebMvcTest(controllers = {PerformResetPasswordWebController.class})
@PropertySource("classpath:messages.properties")
@AutoConfigureMockMvc(addFilters = false)
class PerformPasswordResetViewTest {

	private static final String BASE_URL = "/user/register/password/reset/perform";
	
	@MockBean
	private VerifyResetPasswordTokenUsePort verifyToken;
	
	@MockBean
	private ChangeUserPasswordUsePort changePassword;
	
	@SpyBean
	private PerformResetPasswordWebController resetController;
	
	@Autowired
	private WebClient webClient;
	

	@Test
	void test_getView_whenTokenIsValid() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(verifyToken.verify(any())).thenReturn(true);
		when(changePassword.change(any(), any())).thenReturn(true);
		
		HtmlPage page = webClient.getPage(BASE_URL+"/token-code");
		
		HtmlForm form = page.getFormByName("password-request");
		assertThat(form.getActionAttribute()).matches(BASE_URL+"/token-code");
		form.getInputByName("password").type("pass2");
		form.getInputByName("confirmedPassword").type("pass2");
		form.getButtonByName("submit-button").click();
		
		
		verify(resetController).updatePassword(
				eq("token-code"), 
				eq(new InputPasswordsDto("pass2", "pass2")),
				isA(BindingResult.class));
	}
	
	@Test
	void test_post_whenTokenIsInvalid() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(changePassword.change(any(), any())).thenReturn(false);
		
		List<NameValuePair> params = Arrays.asList(
				new NameValuePair("password", "new password"),
				new NameValuePair("confirmedPassword", "new password"));
		WebRequest webRequest = new WebRequest(new URL("http://localhost"+BASE_URL+"/token-code-2"), HttpMethod.POST);
		webRequest.setRequestParameters(params);
		HtmlPage page = webClient.getPage(webRequest);
		
		assertThat(page.getElementById("content").getTextContent())
			.contains("It was not possible to modify the password");
		assertThat(page.getAnchorByHref("/login").getTextContent()).matches("Back to Login page");
		
		
		verify(resetController).updatePassword(
				eq("token-code-2"), 
				eq(new InputPasswordsDto("new password", "new password")),
				isA(BindingResult.class));
	}
	
	@Test
	void test_post_whenTokenIsValid() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(changePassword.change(any(), any())).thenReturn(true);
		
		List<NameValuePair> params = Arrays.asList(
				new NameValuePair("password", "new password"),
				new NameValuePair("confirmedPassword", "new password"));
		WebRequest webRequest = new WebRequest(new URL("http://localhost"+BASE_URL+"/token-code-2"), HttpMethod.POST);
		webRequest.setRequestParameters(params);
		HtmlPage page = webClient.getPage(webRequest);
		
		assertThat(page.getElementById("content").getTextContent())
			.contains("The password was successfuly updated");
		assertThat(page.getAnchorByHref("/login").getTextContent()).matches("Back to Login page");
		
		
		verify(resetController).updatePassword(
				eq("token-code-2"), 
				eq(new InputPasswordsDto("new password", "new password")),
				isA(BindingResult.class));
	}
}
