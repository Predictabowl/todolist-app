package it.aldinucci.todoapp.adapter.in.web.controller;


import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.VerifyUserEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.VerifyTokenDTOIn;
import it.aldinucci.todoapp.domain.User;

@WebMvcTest (controllers = {UserRegisterVerificationController.class})
@ExtendWith(SpringExtension.class)
class UserRegisterVerificationControllerTest {

	private static final String FIXTURE_TOKEN="test-token-02";
	
	@MockBean
	private VerifyUserEmailUsePort verifyUser;
	
	@MockBean
	private LoadUserByEmailUsePort loadUser;
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	void test_verificationSuccessful() throws Exception {
		
		mvc.perform(get("/user/register/verification/"+FIXTURE_TOKEN))
			.andExpect(status().is3xxRedirection());
		
		verify(verifyUser).verify(new VerifyTokenDTOIn(FIXTURE_TOKEN));
	}
	
	@Test
	void test_otherConditions() {
		//WIP
		/*
		 * Tests for when the token is not valid
		 */
	}
	
	@Test
	void test_resendVerificationGetMapping() throws Exception {
		ModelAndView modelAndView = mvc.perform(get("/user/register/resend/verification"))
			.andExpect(status().isOk())
			.andReturn().getModelAndView();
		
		ModelAndViewAssert.assertViewName(modelAndView, "email-form");
		ModelAndViewAssert.assertModelAttributeValue(modelAndView, "actionLink", "/user/register/resend/verification");
	}
	
	@Test
	void test_resendVerificationConfirm() throws Exception {
		when(loadUser.load(isA(UserIdDTO.class)))
			.thenReturn(Optional.of(new User("user@email.it", "username", "pass", false)));
		
		ModelAndView modelAndView = mvc.perform(post("/user/register/resend/verification")
				.with(csrf())
				.param("email", "user@email.it"))
			.andExpect(status().is3xxRedirection())
			.andReturn().getModelAndView();
		
		ModelAndViewAssert.assertViewName(modelAndView, "redirect:/login");
		verify(loadUser).load(new UserIdDTO("user@email.it"));
	}

}
