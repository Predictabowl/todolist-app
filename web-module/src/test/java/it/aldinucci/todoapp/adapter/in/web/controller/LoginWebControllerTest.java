package it.aldinucci.todoapp.adapter.in.web.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import it.aldinucci.todoapp.config.CommonsBeansProvider;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.util.AppPasswordEncoder;
import it.aldinucci.todoapp.webcommons.model.UserDetailsImpl;

@WebMvcTest (controllers = {LoginWebController.class})
@ExtendWith(SpringExtension.class)
@Import(CommonsBeansProvider.class)
class LoginWebControllerTest {

	private static final String USER_EMAIL = "user@email.it";
	private static final String USER_PASSWORD = "something";
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private AppPasswordEncoder encoder;
	
	@MockBean
	private UserDetailsService detailService;
	
	
	@Test
	void test_login_page() throws Exception {
		mvc.perform(get("/login"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("login/login"));
	}
	
	@Test
	void test_loginSuccessful_shouldRedirect() throws Exception {
		when(detailService.loadUserByUsername(anyString()))
			.thenReturn(new UserDetailsImpl(USER_EMAIL, encoder.encode(USER_PASSWORD), true));
		
		mvc.perform(formLogin("/login")
				.user(USER_EMAIL)
				.password(USER_PASSWORD))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/web"));
	}
	
}
