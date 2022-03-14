package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUserByTaskIdDriverPort;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;

class LoadUserByTaskIdServiceTest {

	@Mock
	private LoadUserByTaskIdDriverPort driverPort;
	
	@InjectMocks
	private LoadUserByTaskIdService loadService;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	
	@Test
	void test_loadUser_successful() throws AppTaskNotFoundException {
		TaskIdDTO taskId = new TaskIdDTO(1L);
		User user = new User("email", "username", "password");
		when(driverPort.load(anyLong())).thenReturn(user);
		
		User loadedUser = loadService.load(taskId);
		
		verify(driverPort).load(1);
		assertThat(loadedUser).isSameAs(user);
	}

}
