package it.aldinucci.todoapp.adapter.in.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;

import it.aldinucci.todoapp.application.port.in.CreateUserUsePort;
import it.aldinucci.todoapp.application.port.in.SendVerificationEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.EmailLinkDTO;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDtoOut;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppEmailAlreadyRegisteredException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;
import it.aldinucci.todoapp.webcommons.dto.RegisterUserDto;

@WebMvcTest (controllers = {RegisterUserWebController.class})
class RegisterUserWebControllerTest {

	private static final String FIXTURE_REGISTER_VIEW = "login/register";
	private static final String FIXTURE_EMAIL = "test@email.it";
	private static final String FIXTURE_URI = "/user/register";

	@MockBean
	private CreateUserUsePort createUser;
	
	@MockBean
	private AppGenericMapper<RegisterUserDto, NewUserDTOIn> mapper;
	
	@MockBean
	private SendVerificationEmailUsePort sendMail;
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	void test_getRegisterPage() throws Exception {
		ModelAndView modelAndView = mvc.perform(get(FIXTURE_URI))
			.andExpect(status().is2xxSuccessful())
			.andReturn().getModelAndView();
		
		assertThat(modelAndView.getModel().containsKey("emailExists")).isFalse();
		ModelAndViewAssert.assertViewName(modelAndView, FIXTURE_REGISTER_VIEW);
	}
	
	@Test
	void test_postRegister_whenInvalidInputEmail() throws Exception {
		RegisterUserDto registerUserDto = new RegisterUserDto("invalidEmail", "test name", "test", "test");
				
		ModelAndView modelAndView = mvc.perform(
			post(FIXTURE_URI)
				.with(csrf())
				.param("email", "invalidEmail")
				.param("username", "test name")
				.param("password", "test")
				.param("confirmedPassword", "test"))
			.andExpect(status().is2xxSuccessful())
			.andReturn().getModelAndView();
		
		ModelAndViewAssert.assertViewName(modelAndView, FIXTURE_REGISTER_VIEW);
		ModelAndViewAssert.assertModelAttributeValue(modelAndView, "registerUserDto", registerUserDto);
		assertThat(modelAndView.getModel().containsKey("emailExists")).isFalse();
		
		verifyNoInteractions(createUser);
		verifyNoInteractions(mapper);
		verifyNoInteractions(sendMail);
	}
	
	@Test
	void test_postRegister_whenPasswordDontMatch() throws Exception {
		RegisterUserDto registerUserDto = new RegisterUserDto("email@test.it", "test name", "test 1", "test 2");
				
		mvc.perform(
			post(FIXTURE_URI)
				.with(csrf())
				.param("email", "email@test.it")
				.param("username", "test name")
				.param("password", "test 1")
				.param("confirmedPassword", "test 2"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name(FIXTURE_REGISTER_VIEW))
			.andExpect(model().attribute("registerUserDto", registerUserDto))
			.andExpect(model().attributeDoesNotExist("emailExists"));
		
		verifyNoInteractions(createUser);
		verifyNoInteractions(mapper);
		verifyNoInteractions(sendMail);
	}
	
	@Test
	void test_postRegister_whenPasswordIsInvalid() throws Exception {
		RegisterUserDto registerUserDto = new RegisterUserDto("email@test.it", "test name", "", "");
				
		mvc.perform(
			post(FIXTURE_URI)
				.with(csrf())
				.param("email", "email@test.it")
				.param("username", "test name")
				.param("password", "")
				.param("confirmedPassword", ""))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name(FIXTURE_REGISTER_VIEW))
			.andExpect(model().attribute("registerUserDto", registerUserDto))
			.andExpect(model().attributeDoesNotExist("emailExists"));
		
		verifyNoInteractions(createUser);
		verifyNoInteractions(mapper);
		verifyNoInteractions(sendMail);
	}
	
	@Test
	void test_postRegister_whenEmailAlreadyExists() throws Exception {
		RegisterUserDto registerUserDto = new RegisterUserDto(FIXTURE_EMAIL, "test name", "testPass", "testPass");
		NewUserDTOIn newUserDTOIn = new NewUserDTOIn("test name", FIXTURE_EMAIL, "testPass");
		when(mapper.map(isA(RegisterUserDto.class))).thenReturn(newUserDTOIn);
		when(createUser.create(isA(NewUserDTOIn.class)))
			.thenThrow(new AppEmailAlreadyRegisteredException("message"));
		
		ModelAndView modelAndView = mvc.perform(
			post(FIXTURE_URI)
				.with(csrf())
				.param("email", FIXTURE_EMAIL)
				.param("username", "test name")
				.param("password", "testPass")
				.param("confirmedPassword", "testPass"))
			.andExpect(status().is2xxSuccessful())
			.andReturn().getModelAndView();
		
		
		ModelAndViewAssert.assertViewName(modelAndView, FIXTURE_REGISTER_VIEW);
		ModelAndViewAssert.assertModelAttributeValue(modelAndView, "registerUserDto", registerUserDto);
		ModelAndViewAssert.assertModelAttributeValue(modelAndView,"emailExists", true);

		InOrder inOrder = Mockito.inOrder(mapper,createUser);
		inOrder.verify(mapper).map(registerUserDto);
		inOrder.verify(createUser).create(newUserDTOIn);
		verifyNoInteractions(sendMail);
	}
	
	/**
	 * Note to self: Changing the server name is needed otherwise the test can't
	 * properly check the building of the url since otherwise both the request and the server
	 * would be on localhost, and thus we won't be sure that the generated link would be correct.
	 * 
	 * @throws Exception
	 */
	@Test
	void test_postRegister_success() throws Exception {
		RegisterUserDto registerUserDto = new RegisterUserDto(FIXTURE_EMAIL, "test name", "testPass", "testPass");
		NewUserDTOIn newUserDTOIn = new NewUserDTOIn("test name", FIXTURE_EMAIL, "testPass");
		when(mapper.map(isA(RegisterUserDto.class))).thenReturn(newUserDTOIn);
		User user = new User(FIXTURE_EMAIL, "name", "pass");
		VerificationToken token = new VerificationToken("token-code", Calendar.getInstance().getTime(), FIXTURE_EMAIL);
		NewUserDtoOut newUser = new NewUserDtoOut(user, token); 
		when(createUser.create(isA(NewUserDTOIn.class))).thenReturn(newUser);
		
		mvc.perform(post(FIXTURE_URI)
				.with(csrf())
				.with(req -> {
					req.setServerName("hostsomewhere.org");
					req.setServerPort(123);
					return req;
				})
				.param("email", FIXTURE_EMAIL)
				.param("username", "test name")
				.param("password", "testPass")
				.param("confirmedPassword", "testPass"))
			.andExpect(status().isOk())
			.andExpect(view().name("login/register.sent.verification"))
			.andExpect(model().attribute("useremail", FIXTURE_EMAIL));
		
		InOrder inOrder = Mockito.inOrder(mapper,createUser, sendMail);
		inOrder.verify(mapper).map(registerUserDto);
		inOrder.verify(createUser).create(newUserDTOIn);
		inOrder.verify(sendMail)
			.send(new EmailLinkDTO("http://hostsomewhere.org:123/user/register/verification/token-code", FIXTURE_EMAIL));

	}

}
