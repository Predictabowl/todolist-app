package it.aldinucci.todoapp.adapter.in.web.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.ui.Model;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import it.aldinucci.todoapp.adapter.in.web.controller.UserRegisterVerificationController;
import it.aldinucci.todoapp.application.port.in.VerifyUserEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.StringTokenDTOIn;

/**
 * We don't need to verify the correct use of the mocked beans, that's responsibility of controller's tests.
 * This is a view test so we just stub the methods to obtain the desired outcome. 
 *
 * @author piero
 *
 */

@WebMvcTest(controllers = {UserRegisterVerificationController.class})
@PropertySource("classpath:messages.properties")
class UserRegisterVerificationViewTest {
	
	private static final String FIXTURE_TOKEN="token-code";
	
	@Autowired
	private WebClient webClient;
	
	@Autowired
	private Environment env;
	
	@MockBean
	private VerifyUserEmailUsePort verifyEmail;
	
	@SpyBean
	private UserRegisterVerificationController verificationController;
	
	private HtmlPage page;

	@BeforeEach
	void setUp(){
		LocaleContextHolder.setLocale(Locale.ENGLISH);
	}
	
	@Test
	void test_viewWhenVerify_successful() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		StringTokenDTOIn token = new StringTokenDTOIn(FIXTURE_TOKEN);
		when(verifyEmail.verify(token)).thenReturn(true);
		
		page = webClient.getPage("/user/register/verification/"+FIXTURE_TOKEN);
		
		assertThat(page.getBody().getTextContent())
			.contains(env.getProperty("the.account.is.now.active"));
		
		assertThat(page.getAnchorByHref("/login").getTextContent())
			.contains(env.getProperty("back.to.login"));
		
		assertThatThrownBy(() -> page.getAnchorByHref("/user/register/resend/verification"))
			.isInstanceOf(ElementNotFoundException.class);
		
		verify(verificationController).confirmRegistration(eq(token),isA(Model.class));
	}
	
	@Test
	void test_viewWhenVerifyFails_theTokenMustBeInvalid() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		StringTokenDTOIn token = new StringTokenDTOIn(FIXTURE_TOKEN);
		when(verifyEmail.verify(token)).thenReturn(false);
		
		page = webClient.getPage("/user/register/verification/"+FIXTURE_TOKEN);
		
		assertThat(page.getBody().getTextContent())
			.contains(env.getProperty("verification.token.is.invalid"));
		
		assertThat(page.getAnchorByHref("/login").getTextContent())
			.contains(env.getProperty("back.to.login"));
		
		assertThat(page.getAnchorByHref("/user/register/resend/verification").getTextContent())
			.contains(env.getProperty("send.email.verification.again"));
		
		verify(verificationController).confirmRegistration(eq(token),isA(Model.class));
	}

}
