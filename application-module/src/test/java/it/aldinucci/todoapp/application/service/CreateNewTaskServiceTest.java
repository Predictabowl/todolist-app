package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.OptionalInt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.CreateTaskDriverPort;
import it.aldinucci.todoapp.application.port.out.GetTaskMaxOrderInProjectDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskData;
import it.aldinucci.todoapp.domain.Task;
import it.aldinucci.todoapp.exception.AppInvalidIdException;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

class CreateNewTaskServiceTest {

	private static final String TASK_DESCRIPTION = "no description";
	private static final String TASK_NAME = "test task";

	@Mock
	private CreateTaskDriverPort newTaskport;
	
	@Mock
	private GetTaskMaxOrderInProjectDriverPort maxOrderTask;
	
	@InjectMocks
	private CreateNewTaskService service;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_createTask_whenInvalidId_shouldThrow() throws AppProjectNotFoundException, AppInvalidIdException {
		when(maxOrderTask.get(anyString())).thenThrow(AppInvalidIdException.class);
		NewTaskDTOIn newTaskDTOIn = new NewTaskDTOIn(TASK_NAME, TASK_DESCRIPTION, "1");
		
		assertThatThrownBy(() -> service.create(newTaskDTOIn))
			.isInstanceOf(AppProjectNotFoundException.class)
			.hasMessage("Project not found with id: 1")
			.hasCauseInstanceOf(AppInvalidIdException.class);
		
		verify(maxOrderTask).get("1");
		verifyNoInteractions(newTaskport);
	}
	
	@Test
	void test_createTaskWhenProjectHaveOtherTasks() throws AppProjectNotFoundException, AppInvalidIdException {
		Task savedTask = new Task("2L", TASK_NAME, "new description");
		when(newTaskport.create(isA(NewTaskData.class))).thenReturn(savedTask);
		when(maxOrderTask.get(anyString())).thenReturn(OptionalInt.of(9));
		
		Task resultTask = service.create(new NewTaskDTOIn(TASK_NAME, TASK_DESCRIPTION, "1"));
		
		InOrder inOrder = inOrder(newTaskport, maxOrderTask);
		inOrder.verify(maxOrderTask).get("1");
		inOrder.verify(newTaskport).create(new NewTaskData(TASK_NAME, TASK_DESCRIPTION, false, "1", 10));
		assertThat(resultTask).isSameAs(savedTask);
	}
	
	@Test
	void test_createFirstTaskOfProject() throws AppProjectNotFoundException, AppInvalidIdException {
		Task savedTask = new Task("2L", TASK_NAME, "new description");
		when(newTaskport.create(isA(NewTaskData.class))).thenReturn(savedTask);
		when(maxOrderTask.get(anyString())).thenReturn(OptionalInt.empty());
		
		Task resultTask = service.create(new NewTaskDTOIn(TASK_NAME, TASK_DESCRIPTION, "1"));
		
		InOrder inOrder = inOrder(newTaskport, maxOrderTask);
		inOrder.verify(maxOrderTask).get("1");
		inOrder.verify(newTaskport).create(new NewTaskData(TASK_NAME, TASK_DESCRIPTION, false, "1", 0));
		assertThat(resultTask).isSameAs(savedTask);
	}

}
