package it.aldinucci.todoapp.application.service;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.out.LoadTaskByIdDriverPort;
import it.aldinucci.todoapp.application.port.out.UpdateTaskDriverPort;
import it.aldinucci.todoapp.domain.Task;

class ToggleTaskCompleteStatusServiceTest {

	@Mock
	private UpdateTaskDriverPort updateTask;
	
	@Mock
	private LoadTaskByIdDriverPort loadTask;
	
	@InjectMocks
	private ToggleTaskCompleteStatusService sut;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_whenTaskNotExists_shouldDoNothing() {
		when(loadTask.load(anyLong())).thenReturn(Optional.empty());
		TaskIdDTO taskId = new TaskIdDTO(1);
		
		sut.toggle(taskId);
		
		verify(loadTask).load(1);
		verifyNoInteractions(updateTask);
	}
	
	@Test
	void test_ifTaskNotCompleted_shouldBeSetAsCompleted() {
		when(loadTask.load(anyLong())).thenReturn(Optional.of(
				new Task(1L, "name", "description",false)));
		TaskIdDTO taskId = new TaskIdDTO(1);
		
		sut.toggle(taskId);
		
		verify(loadTask).load(1);
		verify(updateTask).update(new Task(1L, "name", "description", true));
	}
	
	@Test
	void test_idTaskCompleted_shouldBeSetAsNotCompleted() {
		when(loadTask.load(anyLong())).thenReturn(Optional.of(
				new Task(3L, "name 2", "description 2",true)));
		TaskIdDTO taskId = new TaskIdDTO(2);
		
		sut.toggle(taskId);
		
		verify(loadTask).load(2);
		verify(updateTask).update(new Task(3L, "name 2", "description 2", false));
	}

}
