package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadTasksByProjectIdDriverPort;
import it.aldinucci.todoapp.domain.Task;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LoadProjectTasksService.class})
class LoadProjectTasksServiceTest {

	@MockBean
	private LoadTasksByProjectIdDriverPort driverPort;
	
	@Autowired
	private LoadProjectTasksService service;
	
	@Test
	void test_serviceShouldDelegatetoDriverPort() {
		ProjectIdDTO projectId = new ProjectIdDTO(3L);
		List<Task> taskList = Collections.emptyList();
		when(driverPort.load(anyLong())).thenReturn(taskList);
		
		List<Task> loadedTasks = service.load(projectId);
		
		verify(driverPort).load(3L);
		assertThat(loadedTasks).isSameAs(taskList);
	}

}
