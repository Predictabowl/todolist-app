package it.aldinucci.todoapp.webcommons.dto.mapper;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.application.port.in.dto.ProjectDataDTOIn;
import it.aldinucci.todoapp.webcommons.dto.ProjectDataWebDto;

class ProjectDataWebToInMapperTest {

	private ProjectDataWebToInMapper mapper;
	
	@Test
	void test_mapper() {
		mapper = new ProjectDataWebToInMapper();
		ProjectDataWebDto webData = new ProjectDataWebDto("test");
		
		ProjectDataDTOIn dtoIn = mapper.map(webData);
		
		assertThat(dtoIn.getName()).matches("test");
	}

}
