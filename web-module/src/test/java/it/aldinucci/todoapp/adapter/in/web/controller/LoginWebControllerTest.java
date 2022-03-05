package it.aldinucci.todoapp.adapter.in.web.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

import it.aldinucci.todoapp.adapter.in.web.dto.RegisterUserDto;
import it.aldinucci.todoapp.adapter.in.web.validator.RegisterUserValidator;
import it.aldinucci.todoapp.application.port.in.CreateUserUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@WebMvcTest (controllers = {LoginWebController.class})
@ExtendWith(SpringExtension.class)
class LoginWebControllerTest {

	@MockBean
	private CreateUserUsePort createUser;
	
	@MockBean
	private AppGenericMapper<RegisterUserDto, NewUserDTOIn> mapper;
	
	@MockBean
	private RegisterUserValidator userValidator;
	
	@Autowired
	private MockMvc mvc;
	
	@BeforeEach
	void setUp() {
		when(userValidator.supports(RegisterUserDto.class)).thenReturn(true);
	}
	
	@Test
	void test_login_page() throws Exception {
		ModelAndViewAssert.assertViewName(
			mvc.perform(get("/login"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getModelAndView(),
			"login");
	}
	

}
