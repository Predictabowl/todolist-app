package it.aldinucci.todoapp.adapter.in.web.controller;

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
import it.aldinucci.todoapp.adapter.in.web.service.VerificationHelperService;
import it.aldinucci.todoapp.adapter.in.web.validator.RegisterUserValidator;
import it.aldinucci.todoapp.application.port.in.CreateUserUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppEmailAlreadyRegisteredException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@WebMvcTest (controllers = {RegisterUserWebController.class})
@ExtendWith(SpringExtension.class)
class RegisterUserWebControllerTest {

	private static final String FIXTURE_EMAIL = "test@email.it";
	private static final String FIXTURE_URI = "/user/register";

	@MockBean
	private CreateUserUsePort createUser;
	
	@MockBean
	private AppGenericMapper<RegisterUserDto, NewUserDTOIn> mapper;
	
	@MockBean
	private RegisterUserValidator userValidator;
	
	@MockBean
	private VerificationHelperService verificationService;
	
	@Autowired
	private MockMvc mvc;
	
	@BeforeEach
	void setUp() {
		when(userValidator.supports(RegisterUserDto.class)).thenReturn(true);
	}

	@Test
	void test_getRegisterPage() throws Exception {
		doNothing().when(userValidator).validate(any(), any());
		
		ModelAndView modelAndView = mvc.perform(get(FIXTURE_URI))
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
			post(FIXTURE_URI)
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
		verifyNoInteractions(verificationService);
	}
	
	@Test
	void test_postRegister_whenEmailAlreadyExists() throws Exception {
		RegisterUserDto registerUserDto = new RegisterUserDto(FIXTURE_EMAIL, "test name", "testPass", "testPass");
		NewUserDTOIn newUserDTOIn = new NewUserDTOIn("test name", FIXTURE_EMAIL, "testPass");
		doNothing().when(userValidator).validate(any(), any());
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
		
		ModelAndViewAssert.assertViewName(modelAndView, "register");
		ModelAndViewAssert.assertModelAttributeValue(modelAndView, "registerUserDto", registerUserDto);
		ModelAndViewAssert.assertModelAttributeValue(modelAndView,"emailExists", true);

		InOrder inOrder = Mockito.inOrder(userValidator,mapper,createUser);
		inOrder.verify(userValidator).supports(RegisterUserDto.class);
		inOrder.verify(userValidator).validate(eq(registerUserDto), isA(Errors.class));
		inOrder.verify(mapper).map(registerUserDto);
		inOrder.verify(createUser).create(newUserDTOIn);
		verifyNoInteractions(verificationService);
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
		doNothing().when(userValidator).validate(any(), any());
		when(mapper.map(isA(RegisterUserDto.class))).thenReturn(newUserDTOIn);
		when(createUser.create(isA(NewUserDTOIn.class))).thenReturn(new User(FIXTURE_EMAIL, "name", "pass"));
		
		ModelAndView modelAndView = mvc.perform(post(FIXTURE_URI)
				.with(csrf())
				.with(req -> {
					req.setServerName("hostsomewhere.org");
					return req;
				})
				.param("email", FIXTURE_EMAIL)
				.param("username", "test name")
				.param("password", "testPass")
				.param("confirmedPassword", "testPass"))
			.andExpect(status().isOk())
			.andReturn().getModelAndView();
		
		ModelAndViewAssert.assertViewName(modelAndView, "register.sent.verification");
		ModelAndViewAssert.assertModelAttributeValue(modelAndView, "useremail", FIXTURE_EMAIL);
		
		InOrder inOrder = Mockito.inOrder(userValidator,mapper,createUser, verificationService);
		inOrder.verify(userValidator).supports(RegisterUserDto.class);
		inOrder.verify(userValidator).validate(eq(registerUserDto), isA(Errors.class));
		inOrder.verify(mapper).map(registerUserDto);
		inOrder.verify(createUser).create(newUserDTOIn);
		inOrder.verify(verificationService)
			.sendVerifcationMail(FIXTURE_EMAIL, "http://hostsomewhere.org/user/register/verification");

	}

}
