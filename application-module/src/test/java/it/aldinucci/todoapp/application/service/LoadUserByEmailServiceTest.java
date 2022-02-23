package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LoadUserByEmailService.class})
class LoadUserByEmailServiceTest {

	@MockBean
	private LoadUserByEmailDriverPort driverPort;
	
	@Autowired
	private LoadUserByEmailService loadService;
	
	@Test
	void test_loadUser_successful() {
		User user = new User("new@mail.it", "username", "password");
		when(driverPort.load(anyString())).thenReturn(user);
		
		User loadedUser = loadService.load(new UserIdDTO("test@email.it"));
		
		verify(driverPort).load("test@email.it");
		assertThat(loadedUser).isSameAs(user);
	}

}
