package it.aldinucci.todoapp.adapter.in.web.util;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import it.aldinucci.todoapp.adapter.in.web.controller.RegisterUserWebController;
import it.aldinucci.todoapp.application.port.in.dto.EmailLinkDTO;


@WebMvcTest(controllers = {RegisterUserWebController.class})
class AppLinksBuilderTest {
	
	@MockBean
	private RegisterUserWebController test;
	
	@Test
	void test_verificationLink() {
		EmailLinkDTO linkDto = AppLinksBuilder.buildVerificationLink("http://homepage.com", "token-code", "test@email.it");
		
		assertThat(linkDto.getEmail()).matches("test@email.it");
		assertThat(linkDto.getLink()).matches("http://homepage.com/user/register/verification/token-code");
	}
	
	@Test
	void test_resetPasswordLink() {
		EmailLinkDTO linkDto = AppLinksBuilder.buildResetPasswordLink("http://homepage.com", "token-code", "test@email.it");
		
		assertThat(linkDto.getEmail()).matches("test@email.it");
		assertThat(linkDto.getLink()).matches("http://homepage.com/user/register/password/reset/perform/token-code");
	}

}
