package it.aldinucci.todoapp.application.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.dto.TaskIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteTaskByIdDriverPort;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;

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
	void test_deleteSuccessful() throws AppTaskNotFoundException {
		doNothing().when(deleteTask).delete(anyString());
		
		deleteService.delete(new TaskIdDTO("2"));
		
		verify(deleteTask).delete("2");
	}

}
