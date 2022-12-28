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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import it.aldinucci.todoapp.application.port.in.ChangeUserPasswordUsePort;
import it.aldinucci.todoapp.application.port.in.VerifyResetPasswordTokenUsePort;
import it.aldinucci.todoapp.application.port.in.dto.StringTokenDTOIn;
import it.aldinucci.todoapp.application.port.in.model.AppPassword;
import it.aldinucci.todoapp.webcommons.dto.InputPasswordsDto;


@WebMvcTest(controllers = {PerformResetPasswordWebController.class})
class PerformResetPasswordWebControllerTest {

	private static final String BASE_URL = "/user/register/password/reset/perform";
	private static final String REQUEST_PASSWORD_VIEW = "login/password.request";
	
	@MockBean
	private VerifyResetPasswordTokenUsePort verifyToken;
	
	@MockBean
	private ChangeUserPasswordUsePort changePassword;
	
	@Autowired
	private MockMvc mvc;
	
	
	@Test
	void test_getPage_whenTokenIsInvalid() throws Exception {
		when(verifyToken.verify(any())).thenReturn(false);
		
		mvc.perform(get(BASE_URL+"/token-code")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/login"));
		
		verify(verifyToken).verify(new StringTokenDTOIn("token-code"));
	}
	
	@Test
	void test_getPage_whenTokenIsValid() throws Exception {
		when(verifyToken.verify(any())).thenReturn(true);
		
		mvc.perform(get(BASE_URL+"/token-code")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			
			.andExpect(view().name(REQUEST_PASSWORD_VIEW))
			.andExpect(model().attribute("actionLink",BASE_URL+"/token-code"));
		
		verify(verifyToken).verify(new StringTokenDTOIn("token-code"));
	}
	
	@Test
	void test_post_requireCsrf() throws Exception {
		mvc.perform(post(BASE_URL+"/token-code")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isForbidden());
	}
	
	@Test
	void test_post_whenPasswordsNotMatch() throws Exception {
		mvc.perform(post(BASE_URL+"/token-code")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.param("password", "password")
				.param("confirmedPassword", "pessword"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("inputPasswordsDto", 
					new InputPasswordsDto("password", "pessword")))
			.andExpect(view().name(REQUEST_PASSWORD_VIEW))
			.andExpect(model().attribute("actionLink",BASE_URL+"/token-code"));
	
		verifyNoInteractions(changePassword);
	}
	
	@Test
	void test_post_whenPasswordInvalid() throws Exception {
		mvc.perform(post(BASE_URL+"/token-code")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.param("password", "pass") //too short constraint violation
				.param("confirmedPassword", "pass"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("inputPasswordsDto", 
					new InputPasswordsDto("pass", "pass")))
			.andExpect(view().name(REQUEST_PASSWORD_VIEW))
			.andExpect(model().attribute("actionLink",BASE_URL+"/token-code"));
	
		verifyNoInteractions(changePassword);
	}
	
	@Test
	void test_post_whenTokenIsInvalid() throws Exception {
		when(changePassword.change(any(), any())).thenReturn(false);
		
		mvc.perform(post(BASE_URL+"/token-code")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.param("password", "new password")
				.param("confirmedPassword", "new password"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("inputPasswordsDto", 
					new InputPasswordsDto("new password", "new password")))
			.andExpect(view().name("login/change.password.result"))
			.andExpect(model().attributeDoesNotExist("passwordChanged"))
			.andExpect(model().attributeDoesNotExist("actionLink"));
	
		verify(changePassword).change(new StringTokenDTOIn("token-code"), new AppPassword("new password"));
	}
	
	@Test
	void test_post_whenTokenIsValid() throws Exception {
		when(changePassword.change(any(), any())).thenReturn(true);
		
		mvc.perform(post(BASE_URL+"/token-code")
				.with(csrf())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.param("password", "new password")
				.param("confirmedPassword", "new password"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("inputPasswordsDto", 
					new InputPasswordsDto("new password", "new password")))
			.andExpect(view().name("login/change.password.result"))
			.andExpect(model().attributeExists("passwordChanged"))
			.andExpect(model().attributeDoesNotExist("actionLink"));
	
		verify(changePassword).change(new StringTokenDTOIn("token-code"), new AppPassword("new password"));
	}

}
