package it.aldinucci.todoapp.adapter.in.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest (controllers = {LoginWebController.class})
@ExtendWith(SpringExtension.class)
class LoginWebControllerTest {

	
	@Autowired
	private MockMvc mvc;
	
	
	@Test
	void test_login_page() throws Exception {
		ModelAndViewAssert.assertViewName(
				mvc.perform(get("/login"))
					.andExpect(status().is2xxSuccessful())
						.andReturn().getModelAndView(),
				"login");
	}

}
