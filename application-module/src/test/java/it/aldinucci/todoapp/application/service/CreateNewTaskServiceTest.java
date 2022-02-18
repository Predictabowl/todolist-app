package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.CreateTaskDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskDTOOut;
import it.aldinucci.todoapp.domain.Task;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CreateNewTaskService.class, ModelMapper.class})
class CreateNewTaskServiceTest {

	private static final String TASK_DESCRIPTION = "no description";

	private static final String TASK_NAME = "test task";

	@MockBean
	private CreateTaskDriverPort newTaskport;
		
	@Autowired
	private CreateNewTaskService service;
	
	
	@Test
	void test_serviceShouldUsePort() {
		NewTaskDTOIn newTask = new NewTaskDTOIn(TASK_NAME, TASK_DESCRIPTION, 1L);
		NewTaskDTOOut mappedTask = new NewTaskDTOOut(TASK_NAME, TASK_DESCRIPTION, 1L);
		Task savedTask = new Task(2L, TASK_NAME, "new description");
		when(newTaskport.create(isA(NewTaskDTOOut.class))).thenReturn(savedTask);
		
		Task resultTask = service.create(newTask);
		
		verify(newTaskport).create(mappedTask);
		assertThat(resultTask).isSameAs(savedTask);
	}

}
