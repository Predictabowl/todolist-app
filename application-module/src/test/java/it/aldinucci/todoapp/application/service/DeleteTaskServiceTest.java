package it.aldinucci.todoapp.application.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.port.in.dto.DeleteTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.DeleteTaskByIdDriverPort;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DeleteTaskService.class})
class DeleteTaskServiceTest {

	@MockBean
	private DeleteTaskByIdDriverPort deletePort;
	
	@Autowired
	private DeleteTaskService deleteService;
	
	@Test
	void test_deleteSuccessful() {
		doNothing().when(deletePort).delete(anyLong());
		
		deleteService.delete(new DeleteTaskDTOIn(2L));
		
		verify(deletePort).delete(2L);
	}
	
	@Test
	void test_deleteFailure() {
		doNothing().when(deletePort).delete(anyLong());
		
		deleteService.delete(new DeleteTaskDTOIn(2L));
		
		verify(deletePort).delete(2L);
	}

}
