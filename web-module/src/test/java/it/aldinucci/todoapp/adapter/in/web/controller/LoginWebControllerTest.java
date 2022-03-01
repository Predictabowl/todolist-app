package it.aldinucci.todoapp.adapter.in.web.controller;

import static it.aldinucci.todoapp.webcommons.config.AppBaseURIs.BASE_WEB_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.answerVoid;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.stubbing.VoidAnswer2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

import it.aldinucci.todoapp.adapter.in.web.dto.RegisterUserDto;
import it.aldinucci.todoapp.adapter.in.web.validator.RegisterUserValidator;
import it.aldinucci.todoapp.application.port.in.CreateUserUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppEmailAlreadyRegisteredException;
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
			mvc.perform(get(BASE_WEB_URI+"/login"))
				.andExpect(status().is2xxSuccessful())
				.andReturn().getModelAndView(),
			"login");
	}
	
	@Test
	void test_getRegister() throws Exception {
		doNothing().when(userValidator).validate(any(), any());
		
		ModelAndView modelAndView = mvc.perform(get(BASE_WEB_URI+"/register"))
			.andExpect(status().is2xxSuccessful())
			.andReturn().getModelAndView();
		
		assertThat(modelAndView.getModel().containsKey("emailExists")).isFalse();
		ModelAndViewAssert.assertViewName(modelAndView, "register");
		
		verify(userValidator).supports(RegisterUserDto.class);
	}
	
	@Test
	void test_postRegister_whenInvalidInputModel() throws Exception {
		RegisterUserDto registerUserDto = new RegisterUserDto("invalidEmail", "test name", "test", "test");
		doAnswer(answerVoid(new VoidAnswer2<Object, Errors>() {

			@Override
			public void answer(Object argument0, Errors argument1) throws Throwable {
				argument1.reject("test error");
			}

		})).when(userValidator).validate(any(), any());
		
		ModelAndView modelAndView = mvc.perform(
			post(BASE_WEB_URI+"/register")
				.with(csrf())
				.param("email", "invalidEmail")
				.param("username", "test name")
				.param("password", "test")
				.param("confirmedPassword", "test"))
			.andExpect(status().is2xxSuccessful())
			.andReturn().getModelAndView();
		
		ModelAndViewAssert.assertViewName(modelAndView, "register");
		ModelAndViewAssert.assertModelAttributeValue(modelAndView, "registerUserDto", registerUserDto);
		assertThat(modelAndView.getModel().containsKey("emailExists")).isFalse();
		
		verify(userValidator).supports(RegisterUserDto.class);
		verify(userValidator).validate(eq(registerUserDto), isA(Errors.class));
		verifyNoInteractions(createUser);
		verifyNoInteractions(mapper);
	}
	
	@Test
	void test_postRegister_whenEmailAlreadyExists() throws Exception {
		RegisterUserDto registerUserDto = new RegisterUserDto("test@email.it", "test name", "testPass", "testPass");
		NewUserDTOIn newUserDTOIn = new NewUserDTOIn("test name", "test@email.it", "testPass");
		doNothing().when(userValidator).validate(any(), any());
		when(mapper.map(isA(RegisterUserDto.class))).thenReturn(newUserDTOIn);
		when(createUser.create(isA(NewUserDTOIn.class)))
			.thenThrow(new AppEmailAlreadyRegisteredException("message"));
		
		ModelAndView modelAndView = mvc.perform(
			post(BASE_WEB_URI+"/register")
				.with(csrf())
				.param("email", "test@email.it")
				.param("username", "test name")
				.param("password", "testPass")
				.param("confirmedPassword", "testPass"))
			.andExpect(status().is2xxSuccessful())
			.andReturn().getModelAndView();
		
		ModelAndViewAssert.assertViewName(modelAndView, "register");
		ModelAndViewAssert.assertModelAttributeValue(modelAndView, "registerUserDto", registerUserDto);
		ModelAndViewAssert.assertModelAttributeValue(modelAndView,"emailExists", true);

		InOrder inOrder = Mockito.inOrder(userValidator,mapper,createUser);
		inOrder.verify(userValidator).supports(RegisterUserDto.class);
		inOrder.verify(userValidator).validate(eq(registerUserDto), isA(Errors.class));
		inOrder.verify(mapper).map(registerUserDto);
		inOrder.verify(createUser).create(newUserDTOIn);
	}
	
	@Test
	void test_postRegister_success() throws Exception {
		RegisterUserDto registerUserDto = new RegisterUserDto("test@email.it", "test name", "testPass", "testPass");
		NewUserDTOIn newUserDTOIn = new NewUserDTOIn("test name", "test@email.it", "testPass");
		doNothing().when(userValidator).validate(any(), any());
		when(mapper.map(isA(RegisterUserDto.class))).thenReturn(newUserDTOIn);
		when(createUser.create(isA(NewUserDTOIn.class))).thenReturn(new User());
		
		ModelAndView modelAndView = mvc.perform(
			post(BASE_WEB_URI+"/register")
				.with(csrf())
				.param("email", "test@email.it")
				.param("username", "test name")
				.param("password", "testPass")
				.param("confirmedPassword", "testPass"))
			.andExpect(status().is3xxRedirection())
			.andReturn().getModelAndView();
		
		ModelAndViewAssert.assertViewName(modelAndView, "redirect:/login");
		
		InOrder inOrder = Mockito.inOrder(userValidator,mapper,createUser);
		inOrder.verify(userValidator).supports(RegisterUserDto.class);
		inOrder.verify(userValidator).validate(eq(registerUserDto), isA(Errors.class));
		inOrder.verify(mapper).map(registerUserDto);
		inOrder.verify(createUser).create(newUserDTOIn);
	}

}
