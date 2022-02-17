package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.LoadProjectByIdDriverPort;
import it.aldinucci.todoapp.application.port.out.NewTaskDriverPort;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.domain.Task;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CreateNewTaskService.class})
class CreateNewTaskServiceTest {

	@MockBean
	private NewTaskDriverPort newTaskport;
	
	@MockBean
	private LoadProjectByIdDriverPort loadProjectPort;
	
	@Autowired
	private CreateNewTaskService service;
	
	
	@Test
	void test_serviceShouldUsePort() {
		NewTaskDTOIn newTask = new NewTaskDTOIn("test task", "no description", 1L);
		Task savedTask = new Task(2L, "test task", "new description");
		Project project = new Project(1L, "test project");
		Task task = new Task(null, "test task", "no description");
		task.setProject(project);
		when(newTaskport.save(isA(Task.class))).thenReturn(savedTask);
		when(loadProjectPort.loadById(anyLong())).thenReturn(project);
		
		Task resultTask = service.create(newTask);
		
		InOrder inOrder = inOrder(loadProjectPort,newTaskport);
		inOrder.verify(loadProjectPort).loadById(1L);
		inOrder.verify(newTaskport).save(task);
		assertThat(resultTask).isSameAs(savedTask);
	}

}
