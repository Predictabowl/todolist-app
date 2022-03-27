package it.aldinucci.todoapp.webcommons.mapper;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.application.port.in.dto.TaskDataDTOIn;
import it.aldinucci.todoapp.webcommons.dto.TaskDataWebDto;

class TaskDataWebToInMapperTest {

	@Test
	void test() {
		TaskDataWebToInMapper mapper = new TaskDataWebToInMapper();
		TaskDataWebDto webDto = new TaskDataWebDto("2 test", "go 4 it");
		
		TaskDataDTOIn dtoIn = mapper.map(webDto);
		
		assertThat(dtoIn.getDescription()).matches("go 4 it");
		assertThat(dtoIn.getName()).matches("2 test");
	}

}
