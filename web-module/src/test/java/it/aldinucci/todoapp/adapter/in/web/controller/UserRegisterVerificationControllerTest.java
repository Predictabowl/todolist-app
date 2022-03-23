package it.aldinucci.todoapp.adapter.in.web.controller;


import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import it.aldinucci.todoapp.application.port.in.VerifyUserEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.StringTokenDTOIn;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;

@WebMvcTest (controllers = {UserRegisterVerificationController.class})
@ExtendWith(SpringExtension.class)
class UserRegisterVerificationControllerTest {

	private static final String FIXTURE_RESULT_VIEW = "login/register.result";

	private static final String FIXTURE_TOKEN="test-token-02";
	
	@MockBean
	private VerifyUserEmailUsePort verifyUser;
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	void test_verificationSuccessful() throws Exception {
		when(verifyUser.verify(isA(StringTokenDTOIn.class))).thenReturn(true);
		
		mvc.perform(get("/user/register/verification/"+FIXTURE_TOKEN))
			.andExpect(status().isOk())
			.andExpect(view().name(FIXTURE_RESULT_VIEW))
			.andExpect(model().attribute("accountVerified", true));
		
		verify(verifyUser).verify(new StringTokenDTOIn(FIXTURE_TOKEN));
	}
	
	@Test
	void test_verification_Failure() throws Exception {
		when(verifyUser.verify(isA(StringTokenDTOIn.class))).thenReturn(false);
		
		mvc.perform(get("/user/register/verification/"+FIXTURE_TOKEN))
			.andExpect(status().isOk())
			.andExpect(view().name(FIXTURE_RESULT_VIEW))
			.andExpect(model().attribute("accountVerified", false));
		
		verify(verifyUser).verify(new StringTokenDTOIn(FIXTURE_TOKEN));
	}
	
	@Test
	void test_verificationWhenUserNotExists() throws Exception {
		when(verifyUser.verify(isA(StringTokenDTOIn.class)))
			.thenThrow(new AppUserNotFoundException("some error"));
		
		mvc.perform(get("/user/register/verification/"+FIXTURE_TOKEN))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/user/register"));
		
		verify(verifyUser).verify(new StringTokenDTOIn(FIXTURE_TOKEN));
	}
	
	

}
