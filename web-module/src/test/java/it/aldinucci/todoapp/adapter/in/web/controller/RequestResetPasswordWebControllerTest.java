package it.aldinucci.todoapp.adapter.in.web.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Calendar;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import it.aldinucci.todoapp.application.port.in.GetOrCreatePasswordResetTokenUsePort;
import it.aldinucci.todoapp.application.port.in.SendResetPasswordEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.EmailLinkDTO;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.ResetPasswordToken;

@WebMvcTest(controllers = {RequestResetPasswordWebController.class})
class RequestResetPasswordWebControllerTest {

	private static final String FIXTURE_EMAIL = "email@test.it";

	private static final String EMAIL_VIEW = "login/email.request";

	private static final String BASE_URL = "/user/register/password/reset";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	private GetOrCreatePasswordResetTokenUsePort retrieveToken;
	
	@MockBean
	private SendResetPasswordEmailUsePort sendEmail;
	
	@Test
	void test_getPage() throws Exception {
		mvc.perform(get(BASE_URL)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(view().name(EMAIL_VIEW))
			.andExpect(model().attribute("actionLink", BASE_URL))
			.andExpect(model().attributeDoesNotExist("emailNotFound"));
	}
	
	@Test
	void test_postPage_requireCsrf() throws Exception {
		mvc.perform(post(BASE_URL)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.param("email", "nobody@email.it"))
			.andExpect(status().isForbidden());
		
		verifyNoInteractions(retrieveToken);
		verifyNoInteractions(sendEmail);
	}
	
	@Test
	void test_postPage_whenEmailNotValid() throws Exception {
		mvc.perform(post(BASE_URL)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.param("email", "email.it"))
			.andExpect(status().isOk())
			.andExpect(view().name(EMAIL_VIEW))
			.andExpect(model().attributeDoesNotExist("emailNotFound"))
			.andExpect(model().attribute("actionLink", BASE_URL));		
		
		verifyNoInteractions(retrieveToken);
		verifyNoInteractions(sendEmail);
	}
	
	
	@Test
	void test_postPage_whenUserDontExists() throws Exception {
		when(retrieveToken.get(any())).thenReturn(Optional.empty());
		
		mvc.perform(post(BASE_URL)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.param("email", FIXTURE_EMAIL))
			.andExpect(status().isOk())
			.andExpect(view().name(EMAIL_VIEW))
			.andExpect(model().attribute("emailNotFound", true))
			.andExpect(model().attribute("actionLink", BASE_URL));
		
		verify(retrieveToken).get(new UserIdDTO(FIXTURE_EMAIL));
		verifyNoInteractions(sendEmail);
	}
	
	@Test
	void test_postPage_success() throws Exception {
		ResetPasswordToken token = new ResetPasswordToken("code", Calendar.getInstance().getTime(), FIXTURE_EMAIL);
		when(retrieveToken.get(any())).thenReturn(Optional.of(token));
		
		mvc.perform(post(BASE_URL)
				.with(csrf())
				.with(request -> {
						request.setServerName("server");
						request.setServerPort(333);
						return request;
					})
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.param("email", FIXTURE_EMAIL))
			.andExpect(status().isOk())
			.andExpect(view().name("login/register.sent.verification"))
			.andExpect(model().attributeDoesNotExist("emailNotFound"))
			.andExpect(model().attributeDoesNotExist("actionLink"))
			.andExpect(model().attribute("useremail", FIXTURE_EMAIL));
		
		verify(retrieveToken).get(new UserIdDTO(FIXTURE_EMAIL));
		verify(sendEmail).send(new EmailLinkDTO(
				String.format("http://server:333%s/perform/%s", BASE_URL, "code"), 
				FIXTURE_EMAIL));
	}

}
