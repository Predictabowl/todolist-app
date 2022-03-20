package it.aldinucci.todoapp.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectData;

class ProjectMapperInOutTest {
	
	private ProjectMapperInOut mapper;

	@Test
	void test_mapper() {
		mapper = new ProjectMapperInOut();
		NewProjectDTOIn dtoIn = new NewProjectDTOIn("name", "user@email.it");
		NewProjectData dtoOut = mapper.map(dtoIn);
		
		assertThat(dtoOut.name()).isEqualTo(dtoIn.getName());
		assertThat(dtoOut.userEmail()).isEqualTo(dtoIn.getUserEmail());
	}

}
