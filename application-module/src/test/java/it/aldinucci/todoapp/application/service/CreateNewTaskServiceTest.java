package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.port.in.NewTaskPort;
import it.aldinucci.todoapp.domain.Task;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CreateNewTaskService.class})
class CreateNewTaskServiceTest {

	@Autowired
	private CreateNewTaskService service;
	
	@MockBean
	private NewTaskPort port;
	
	@Test
	void test_serviceShouldUsePort() {
		Task task = new Task(null, "test task", "");
		Task savedTask = new Task(2L, "test task", "new description");
		when(port.save(task)).thenReturn(savedTask);
		
		Task resultTask = service.create(task);
		
		verify(port).save(task);
		assertThat(resultTask).isSameAs(savedTask);
	}

}
