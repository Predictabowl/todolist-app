package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
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

	private AutoCloseable closeable;
	
	@BeforeEach
	void setUp() {
		closeable = openMocks(this);
	}
	
	@AfterEach
	void tearDown() throws Exception {
		closeable.close();
	}
	
	@Test
	void test_loadUser_successful() throws AppTaskNotFoundException {
		TaskIdDTO taskId = new TaskIdDTO("1");
		User user = new User("email", "username", "password");
		Optional<User> optional = Optional.of(user);
		when(driverPort.load(anyString())).thenReturn(optional);
		
		Optional<User> loadedUser = loadService.load(taskId);
		
		verify(driverPort).load("1");
		assertThat(loadedUser).isSameAs(optional);
	}
	
}
