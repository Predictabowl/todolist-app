package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.TaskDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadTaskByIdDriverPort;
import it.aldinucci.todoapp.application.port.out.UpdateTaskDriverPort;
import it.aldinucci.todoapp.domain.Task;

class UpdateTaskServiceTest {

	@Mock
	private UpdateTaskDriverPort updateTask;
	
	@Mock
	private LoadTaskByIdDriverPort loadTask;
	
	@InjectMocks
	private UpdateTaskService sut;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_updateTask_WhenTaskDontExists() {
		when(loadTask.load(anyLong())).thenReturn(Optional.empty());
		
		Optional<Task> updatedTask = sut.update(new TaskIdDTO(4), new TaskDataDTOIn("name", "test desc"));
		
		assertThat(updatedTask).isEmpty();
		verify(loadTask).load(4);
		verifyNoInteractions(updateTask);
	}

	@Test
	void test_updateTask_success() {
		when(loadTask.load(anyLong())).thenReturn(Optional.of(new Task(3L, "test", "descr", true, 2)));
		Task returnedTask = new Task();
		when(updateTask.update(isA(Task.class))).thenReturn(Optional.of(returnedTask));
		
		Optional<Task> updatedTask = sut.update(new TaskIdDTO(4), new TaskDataDTOIn("name", "test desc"));
		
		assertThat(updatedTask).contains(returnedTask);
		verify(loadTask).load(4);
		verify(updateTask).update(new Task(3L, "name", "test desc", true, 2));
	}
}
