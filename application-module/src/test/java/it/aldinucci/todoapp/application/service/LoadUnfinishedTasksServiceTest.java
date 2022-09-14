package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadUnfinishedTasksDriverPort;
import it.aldinucci.todoapp.domain.Task;

class LoadUnfinishedTasksServiceTest {

	@Mock
	private LoadUnfinishedTasksDriverPort driverPort;
	
	@InjectMocks
	private LoadUnfinishedTasksService service;

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
	void test_loadUnfinishedTasks_whenNoTasks() {
		List<Task> tasks = Collections.emptyList();
		
		when(driverPort.load(anyString())).thenReturn(tasks);
		
		List<Task> loadedTasks = service.load(new ProjectIdDTO("2"));
		
		verify(driverPort).load("2");
		assertThat(loadedTasks).isSameAs(tasks);
	}
	
	@Test
	void test_loadUnfinishedTasks_successful() {
		List<Task> tasks = Arrays.asList(
				new Task("3L", "task", "descr", true, 4));
		
		when(driverPort.load(anyString())).thenReturn(tasks);
		
		List<Task> loadedTasks = service.load(new ProjectIdDTO("2"));
		
		verify(driverPort).load("2");
		assertThat(loadedTasks).isSameAs(tasks);
	}

}
