package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteTaskByIdDriverPort;

class DeleteTaskServiceTest {

	@Mock
	private DeleteTaskByIdDriverPort deleteTask;
	
	@InjectMocks
	private DeleteTaskService deleteService;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_deleteSuccessful() {
		when(deleteTask.delete(anyString())).thenReturn(true);
		
		boolean deleted = deleteService.delete(new TaskIdDTO("2"));
		
		assertThat(deleted).isTrue();
		verify(deleteTask).delete("2");
	}
	
	@Test
	void test_deleteFail() {
		when(deleteTask.delete(anyString())).thenReturn(false);
		
		boolean deleted = deleteService.delete(new TaskIdDTO("2"));
		
		assertThat(deleted).isFalse();
		verify(deleteTask).delete("2");
	}

}
