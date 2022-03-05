package it.aldinucci.todoapp.adapter.in.web.controller;


import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import it.aldinucci.todoapp.application.port.in.VerifyUserEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.VerifyTokenDTOIn;

@WebMvcTest (controllers = {UserRegisterVerificationController.class})
@ExtendWith(SpringExtension.class)
class UserRegisterVerificationControllerTest {

	private static final String FIXTURE_TOKEN="test-token-02";
	
	@MockBean
	private VerifyUserEmailUsePort verifyUser;
	
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

}
