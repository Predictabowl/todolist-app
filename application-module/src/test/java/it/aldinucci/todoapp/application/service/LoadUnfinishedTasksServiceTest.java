package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Collections;
import java.util.List;

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
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_loadUnfinishedTasks_successful() {
		List<Task> tasks = Collections.emptyList();
		
		when(driverPort.load(anyLong())).thenReturn(tasks);
		
		List<Task> loadedTasks = service.load(new ProjectIdDTO(2));
		
		verify(driverPort).load(2);
		assertThat(loadedTasks).isSameAs(tasks);
	}

}