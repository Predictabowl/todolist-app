package it.aldinucci.todoapp.adapter.in.rest.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.aldinucci.todoapp.adapter.in.rest.security.config.AppRestSecurityConfig;
import it.aldinucci.todoapp.application.port.in.UpdateUserDataUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.dto.UserDataWebDto;

@WebMvcTest(controllers = UpdateUserDataRestController.class)
@ExtendWith(SpringExtension.class)
@Import({AppRestSecurityConfig.class})
class UpdateUserDataRestControllerTest {

	private static final String FIXTURE_URL = "/api/user";
	private static final String FIXTURE_EMAIL = "test@email.it";
	
	@MockBean
	private UpdateUserDataUsePort updateUser;
	
	@Autowired
	private MockMvc mvc;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	void test_whenNotAuthenticated() throws JsonProcessingException, Exception {
		mvc.perform(put(FIXTURE_URL)
				.with(csrf())
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new UserDataDTOIn("new name"))))
			.andExpect(status().isUnauthorized());
		
		verifyNoInteractions(updateUser);
	}
	
	@Test
	@WithMockUser
	void test_withoutCsrf() throws JsonProcessingException, Exception {
		mvc.perform(put(FIXTURE_URL)
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new UserDataDTOIn("new name"))))
			.andExpect(status().isForbidden());
		
		verifyNoInteractions(updateUser);
	}

	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_whenUserNotFound() throws JsonProcessingException, Exception {
		when(updateUser.update(any(), any())).thenReturn(Optional.empty());
		UserDataWebDto userData = new UserDataWebDto("new name");
		
		mvc.perform(put(FIXTURE_URL)
				.with(csrf())
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userData)))
			.andExpect(status().isNotFound());
		
		verify(updateUser).update(new UserIdDTO(FIXTURE_EMAIL), new UserDataDTOIn("new name"));
	}

	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_whenUsernameNotValid() throws JsonProcessingException, Exception {
		UserDataWebDto userData = new UserDataWebDto("");
		
		mvc.perform(put(FIXTURE_URL)
				.with(csrf())
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userData)))
			.andExpect(status().isBadRequest());
		
		verifyNoInteractions(updateUser);
	}
	
	@Test
	@WithMockUser(FIXTURE_EMAIL)
	void test_updateSuccess() throws JsonProcessingException, Exception {
		when(updateUser.update(any(), any())).thenReturn(Optional.of(new User()));
		
		mvc.perform(put(FIXTURE_URL)
				.with(csrf())
				.contentType(APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new UserDataWebDto("new name"))))
			.andExpect(status().isNoContent());
		
		verify(updateUser).update(new UserIdDTO(FIXTURE_EMAIL), new UserDataDTOIn("new name"));
	}
}
