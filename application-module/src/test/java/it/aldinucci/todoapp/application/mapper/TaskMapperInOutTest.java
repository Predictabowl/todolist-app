package it.aldinucci.todoapp.application.mapper;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.application.port.in.dto.NewTaskDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewTaskData;

class TaskMapperInOutTest {

	private TaskMapperInOut mapper;
	
	@Test
	void test_mapper() {
		mapper = new TaskMapperInOut();
		
		NewTaskData dtoOut = mapper.map(new NewTaskDTOIn("name", "description", 5L));
		
		assertThat(dtoOut.getName()).isEqualTo("name");
		assertThat(dtoOut.getDescription()).isEqualTo("description");
		assertThat(dtoOut.getProjectId()).isEqualTo(5L);
	}

}
