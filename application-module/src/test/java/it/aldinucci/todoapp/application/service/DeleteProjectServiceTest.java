package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.port.in.dto.ProjectIdDTO;
import it.aldinucci.todoapp.application.port.out.DeleteProjectByIdDriverPort;
import it.aldinucci.todoapp.exception.AppProjectNotFoundException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DeleteProjectService.class})
class DeleteProjectServiceTest {

	@MockBean
	private DeleteProjectByIdDriverPort deletePort;
	
	@Autowired
	private DeleteProjectService deleteService;
	
	@Test
	void test_deleteSuccessful() throws AppProjectNotFoundException {
		when(deletePort.delete(anyString())).thenReturn(true);
		
		boolean deleted = deleteService.delete(new ProjectIdDTO("2"));
		
		assertThat(deleted).isTrue();
		verify(deletePort).delete("2");
	}
	
	@Test
	void test_deleteFailure() throws AppProjectNotFoundException {
		when(deletePort.delete(anyString())).thenReturn(false);
		
		boolean deleted = deleteService.delete(new ProjectIdDTO("4"));
		
		assertThat(deleted).isFalse();
		verify(deletePort).delete("4");
	}

}
