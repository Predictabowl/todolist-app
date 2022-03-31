package it.aldinucci.todoapp.adapter.in.web.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import it.aldinucci.todoapp.adapter.in.web.controller.UpdateUserDataWebController;
import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.UpdateUserDataUsePort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.dto.UserDataWebDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {UpdateUserDataWebController.class})
@PropertySource("classpath:messages.properties")
class UpdateUserDataViewTest {

	private static final String BASE_URI = "/web/user/data";

	private static final String FIXTURE_EMAIL = "test@email.it";
	
	@Autowired
	private WebClient webClient;
	
	@MockBean
	private LoadUserByEmailUsePort loadUser;
	
	@MockBean
	private UpdateUserDataUsePort updateUser;
	
	@SpyBean
	private UpdateUserDataWebController updateUserController;
	
	@Autowired
	Environment env;
	
	private HtmlPage page;

	@BeforeEach
	@WithMockUser()
	void setUp() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		User user = new User(FIXTURE_EMAIL, "test name", "password", true);
		when(loadUser.load(any())).thenReturn(Optional.of(user));
		when(updateUser.update(any(), any())).thenReturn(Optional.of(user));
		
		page = webClient.getPage(BASE_URI);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_getView_formSubmit_success() throws IOException {
		HtmlForm form = page.getFormByName("user-data-form");
		assertThat(form.getActionAttribute()).matches(BASE_URI);
		assertThat(form.getMethodAttribute()).matches("post");
		
		HtmlInput input = form.getInputByName("username");
		assertThat(input.getValueAttribute()).matches("test name");
		input.setValueAttribute("new name");
		
		HtmlButton submitButton = form.getButtonByName("submit-button");
		assertThat(submitButton.getTextContent()).matches(env.getProperty("confirm"));
		submitButton.click();
		
		verify(updateUserController).postUserDataUpdate(isA(Authentication.class), 
				eq(new UserDataWebDto("new name")), isA(BindingResult.class));
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_pageElements() throws IOException {
		assertThat(page.getTitleText()).matches(env.getProperty("user.settings"));
		
		assertThat(page.getAnchorByHref("/web").getTextContent())
			.matches(env.getProperty("back.to.home"));
		
	}
	
}
