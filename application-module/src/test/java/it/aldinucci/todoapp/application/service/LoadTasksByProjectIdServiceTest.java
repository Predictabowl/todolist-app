package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadTasksByProjectIdDriverPort;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

class LoadTasksByProjectIdServiceTest {

	@Mock
	private LoadTasksByProjectIdDriverPort driverPort;
	
	@InjectMocks
	private LoadTasksByProjectIdService service;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_serviceShouldDelegatetoDriverPort() throws AppProjectNotFoundException {
		ProjectIdDTO projectId = new ProjectIdDTO("3");
		List<Task> taskList = Collections.emptyList();
		when(driverPort.load(anyString())).thenReturn(taskList);
		
		List<Task> loadedTasks = service.load(projectId);
		
		verify(driverPort).load("3");
		assertThat(loadedTasks).isEmpty();
	}

	@Test
	void test_loadedTasksShouldBeOrdered() throws AppProjectNotFoundException {
		ProjectIdDTO projectId = new ProjectIdDTO("3");
		Task task1 = new Task("2L", "task 1", "descr 1", false, 5);
		Task task2 = new Task("3L", "task 2", "descr 2", true, 2);
		Task task3 = new Task("4L", "task 3", "descr 3", false, 7);
		List<Task> taskList = Arrays.asList(task1, task2, task3);	
		when(driverPort.load(anyString())).thenReturn(taskList);
		
		List<Task> loadedTasks = service.load(projectId);
		
		verify(driverPort).load("3");
		assertThat(loadedTasks).containsExactly(task2, task1, task3);
	}
}
