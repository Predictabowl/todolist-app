package it.aldinucci.todoapp.adapter.in.web.controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.util.NestedServletException;

import it.aldinucci.todoapp.application.port.in.LoadUserByEmailUsePort;
import it.aldinucci.todoapp.application.port.in.UpdateUserDataUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.webcommons.dto.UserDataWebDto;

@WebMvcTest (controllers = {UpdateUserDataWebController.class})
@ExtendWith(SpringExtension.class)
class UpdateUserDataWebControllerTest {

	private static final String FIXTURE_VIEW = "user.settings";
	
	private static final String FIXTURE_URI = "/web/user/data";
	
	private static final String FIXTURE_EMAIL = "email@test.it";

	@MockBean
	private LoadUserByEmailUsePort loadUser;
	
	@MockBean
	private UpdateUserDataUsePort updateUser;
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	void test_getPage_needAuthentication() throws Exception {
		mvc.perform(get(FIXTURE_URI))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_getPage_whenUserNotFound() throws Exception {
		when(loadUser.load(any())).thenReturn(Optional.empty());
		
		MockHttpServletRequestBuilder requestBuilder = get(FIXTURE_URI);
		assertThatThrownBy(() -> mvc.perform(requestBuilder))
			.isInstanceOf(NestedServletException.class)
			.getCause()
				.isInstanceOf(AppUserNotFoundException.class)
				.hasMessage("Could not find user with email: "+FIXTURE_EMAIL);

		verify(loadUser).load(new UserIdDTO(FIXTURE_EMAIL));
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_getPage() throws Exception {
		UserDataWebDto userDataWebDto = new UserDataWebDto("test name");
		User user = new User(FIXTURE_EMAIL, "test name", "password", true);
		when(loadUser.load(any())).thenReturn(Optional.of(user));
		
		mvc.perform(get(FIXTURE_URI))
			.andExpect(status().isOk())
			.andExpect(view().name(FIXTURE_VIEW))
			.andExpect(model().attribute("userDataWebDto", userDataWebDto));
		
		verify(loadUser).load(new UserIdDTO(FIXTURE_EMAIL));
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_postUserData_needsCsrf() throws Exception {
		mvc.perform(post(FIXTURE_URI))
			.andExpect(status().isForbidden());
		
		verifyNoInteractions(loadUser);
		verifyNoInteractions(updateUser);
	}
	
	@Test
	void test_postUserData_needsAuthentication() throws Exception {
		mvc.perform(post(FIXTURE_URI)
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/login"));
		
		verifyNoInteractions(loadUser);
		verifyNoInteractions(updateUser);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_postUserData_success() throws Exception {
		User user = new User(FIXTURE_EMAIL, "new name", "password", true);
		when(updateUser.update(any(), any())).thenReturn(Optional.of(user));
		
		mvc.perform(post(FIXTURE_URI)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.param("username", "new name"))
			.andExpect(status().isOk())
			.andExpect(view().name(FIXTURE_VIEW))
			.andExpect(model().attribute("userDataWebDto", new UserDataWebDto("new name")));
		
		verify(updateUser).update(new UserIdDTO(FIXTURE_EMAIL), new UserDataDTOIn("new name"));
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_postUserData_updateFailure() throws Exception {
		when(updateUser.update(any(), any())).thenReturn(Optional.empty());
		
		MockHttpServletRequestBuilder requestBuilder = post(FIXTURE_URI)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.param("username", "new name");
		
		assertThatThrownBy(() -> mvc.perform(requestBuilder)).getCause()
			.isInstanceOf(AppUserNotFoundException.class)
			.hasMessage("Could not find user with email: "+FIXTURE_EMAIL);
		
		verify(updateUser).update(new UserIdDTO(FIXTURE_EMAIL), new UserDataDTOIn("new name"));
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_postUserData_withInvalidInput() throws Exception {
		User user = new User(FIXTURE_EMAIL, "new name", "password", true);
		when(updateUser.update(any(), any())).thenReturn(Optional.of(user));
		
		mvc.perform(post(FIXTURE_URI)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.param("username", ""))
			.andExpect(status().isOk())
			.andExpect(view().name(FIXTURE_VIEW));
		
		verifyNoInteractions(updateUser);
	}

}
