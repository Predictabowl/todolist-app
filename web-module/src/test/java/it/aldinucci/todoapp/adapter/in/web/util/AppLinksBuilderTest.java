package it.aldinucci.todoapp.adapter.in.web.util;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.in.web.controller.RegisterUserWebController;
import it.aldinucci.todoapp.application.port.in.dto.EmailLinkDTO;


@WebMvcTest(controllers = {RegisterUserWebController.class})
@ExtendWith(SpringExtension.class)
class AppLinksBuilderTest {
	
	@MockBean
	private RegisterUserWebController test;
	
	@Test
	void test() {
		EmailLinkDTO linkDto = AppLinksBuilder.buildVerificationLink("http://homepage.com", "token-code", "test@email.it");
		
		assertThat(linkDto.getEmail()).matches("test@email.it");
		assertThat(linkDto.getLink()).matches("http://homepage.com/user/register/verification/token-code");
	}

}
