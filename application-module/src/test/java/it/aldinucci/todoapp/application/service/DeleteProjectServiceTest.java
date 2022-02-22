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

import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteProjectByIdDriverPort;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DeleteProjectService.class})
class DeleteProjectServiceTest {

	@MockBean
	private DeleteProjectByIdDriverPort deletePort;
	
	@Autowired
	private DeleteProjectService deleteService;
	
	@Test
	void test_deleteSuccessful() {
		doNothing().when(deletePort).delete(anyLong());
		
		deleteService.delete(new ProjectIdDTO(2L));
		
		verify(deletePort).delete(2L);
	}
	
	@Test
	void test_deleteFailure() {
		doNothing().when(deletePort).delete(anyLong());
		
		deleteService.delete(new ProjectIdDTO(4L));
		
		verify(deletePort).delete(4L);
	}

}
