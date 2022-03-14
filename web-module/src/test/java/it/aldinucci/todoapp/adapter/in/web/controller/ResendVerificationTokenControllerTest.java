package it.aldinucci.todoapp.adapter.in.web.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import it.aldinucci.todoapp.application.port.in.RetrieveVerificationTokenUsePort;
import it.aldinucci.todoapp.application.port.in.SendVerificationEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.VerificationLinkDTO;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppUserEmailAlreadyVerifiedException;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

@WebMvcTest(controllers = {ResendVerificationTokenController.class})
@ExtendWith(SpringExtension.class)
class ResendVerificationTokenControllerTest {

	private static final String FIXTURE_EMAIL_REQUEST_VIEW = "login/email.request";

	private static final String FIXTURE_EMAIL = "email@test.org";
	
	@MockBean
	private RetrieveVerificationTokenUsePort retrieveToken;
	
	@MockBean
	private SendVerificationEmailUsePort sendMail;
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	void test_resendVerificationGetMapping() throws Exception {
		ModelAndView modelAndView = mvc.perform(get("/user/register/resend/verification"))
			.andExpect(status().isOk())
			.andReturn().getModelAndView();
		
		ModelAndViewAssert.assertViewName(modelAndView, FIXTURE_EMAIL_REQUEST_VIEW);
		ModelAndViewAssert.assertModelAttributeValue(modelAndView, "actionLink", "/user/register/resend/verification");
	}
	
	@Test
	void test_resendVerificationPostMapping_success() throws Exception {
		UserIdDTO userId = new UserIdDTO(FIXTURE_EMAIL);
		VerificationToken token = new VerificationToken("code", null, FIXTURE_EMAIL);
		when(retrieveToken.get(isA(UserIdDTO.class))).thenReturn(token);

		ModelAndView modelAndView = mvc.perform(post("/user/register/resend/verification")
				.with(csrf())
				.with(req -> {
					req.setServerName("differenthost.org");
					req.setServerPort(23);
					return req;
				})
				.param("email", FIXTURE_EMAIL))
			.andExpect(status().isOk())
			.andReturn().getModelAndView();
		
		ModelAndViewAssert.assertViewName(modelAndView, "login/register.sent.verification");
		ModelAndViewAssert.assertModelAttributeValue(modelAndView, "useremail", FIXTURE_EMAIL);
		verify(retrieveToken).get(userId);
		verify(sendMail).send(new VerificationLinkDTO(
				"http://differenthost.org:23/user/register/verification/code",
				FIXTURE_EMAIL));
	}
	
	@Test
	void test_resendVerificationPostMapping_whenEmailAlreadyVerified() throws Exception {
		UserIdDTO userId = new UserIdDTO(FIXTURE_EMAIL);
		when(retrieveToken.get(isA(UserIdDTO.class))).thenThrow(
				new AppUserEmailAlreadyVerifiedException("test message"));

		ModelAndView modelAndView = mvc.perform(post("/user/register/resend/verification")
				.with(csrf())
				.param("email", FIXTURE_EMAIL))
			.andExpect(status().isOk())
			.andReturn().getModelAndView();
		
		ModelAndViewAssert.assertViewName(modelAndView, FIXTURE_EMAIL_REQUEST_VIEW);
		ModelAndViewAssert.assertModelAttributeValue(modelAndView, "emailAlreadyVerified", true);
		ModelAndViewAssert.assertModelAttributeValue(modelAndView, "actionLink", "/user/register/resend/verification");
		verify(retrieveToken).get(userId);
		verifyNoInteractions(sendMail);
	}
	
	@Test
	void test_resendVerificationPostMapping_whenUserNotFound() throws Exception {
		UserIdDTO userId = new UserIdDTO(FIXTURE_EMAIL);
		when(retrieveToken.get(isA(UserIdDTO.class))).thenThrow(
				new AppUserNotFoundException("not found message"));

		ModelAndView modelAndView = mvc.perform(post("/user/register/resend/verification")
				.with(csrf())
				.param("email", FIXTURE_EMAIL))
			.andExpect(status().isOk())
			.andReturn().getModelAndView();
		
		ModelAndViewAssert.assertViewName(modelAndView, FIXTURE_EMAIL_REQUEST_VIEW);
		ModelAndViewAssert.assertModelAttributeValue(modelAndView, "emailNotFound", true);
		ModelAndViewAssert.assertModelAttributeValue(modelAndView, "actionLink", "/user/register/resend/verification");
		verify(retrieveToken).get(userId);
		verifyNoInteractions(sendMail);
	}
	
	@Test
	void test_resendVerificationPostMapping_whenEmailIsNotValid() throws Exception {
		
		ModelAndView modelAndView = mvc.perform(post("/user/register/resend/verification")
				.with(csrf())
				.param("email", "malformedEmail"))
			.andExpect(status().isOk())
			.andReturn().getModelAndView();
		
		ModelAndViewAssert.assertViewName(modelAndView, FIXTURE_EMAIL_REQUEST_VIEW);
		ModelAndViewAssert.assertModelAttributeValue(modelAndView, "actionLink", "/user/register/resend/verification");		
		assertThat(modelAndView.getModel()).doesNotContainKey("tokenErrorMessage");
		
		verifyNoInteractions(retrieveToken);
		verifyNoInteractions(sendMail);
	}
}
