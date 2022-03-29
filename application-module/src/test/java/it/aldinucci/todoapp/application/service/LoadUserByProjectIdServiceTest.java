package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByProjectIdDriverPort;
import it.aldinucci.todoapp.domain.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LoadUserByProjectIdService.class})
class LoadUserByProjectIdServiceTest {

	@Autowired
	private LoadUserByProjectIdService userService;
	
	@MockBean
	private LoadUserByProjectIdDriverPort userPort;
	
	@Test
	void test_loadUser(){
		ProjectIdDTO projectId = new ProjectIdDTO("4L");
		User user = new User("email", "username", "password");
		when(userPort.load(anyString())).thenReturn(Optional.of(user));
		
		Optional<User> loadedUser = userService.load(projectId);
		
		verify(userPort).load("4L");
		assertThat(loadedUser).containsSame(user);
	}

}
