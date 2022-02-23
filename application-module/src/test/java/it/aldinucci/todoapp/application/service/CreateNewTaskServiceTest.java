package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import it.aldinucci.todoapp.application.mapper.TaskMapper;
import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.CreateTaskDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskDTOOut;
import it.aldinucci.todoapp.domain.Task;

class CreateNewTaskServiceTest {

	private static final String TASK_DESCRIPTION = "no description";

	private static final String TASK_NAME = "test task";

	@Mock
	private CreateTaskDriverPort newTaskport;
	
	@Mock
	private TaskMapper mapper;
		
	@Autowired
	private CreateNewTaskService service;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
		service = new CreateNewTaskService(newTaskport, mapper);
	}
	
	@Test
	void test_serviceShouldUsePort() {
		NewTaskDTOIn newTask = new NewTaskDTOIn(TASK_NAME, TASK_DESCRIPTION, 1L);
		NewTaskDTOOut mappedTask = new NewTaskDTOOut(TASK_NAME, TASK_DESCRIPTION, 1L);
		Task savedTask = new Task(2L, TASK_NAME, "new description");
		when(newTaskport.create(isA(NewTaskDTOOut.class))).thenReturn(savedTask);
		when(mapper.map(newTask)).thenReturn(mappedTask);
		
		Task resultTask = service.create(newTask);
		
		verify(newTaskport).create(mappedTask);
		assertThat(resultTask).isSameAs(savedTask);
	}

}
