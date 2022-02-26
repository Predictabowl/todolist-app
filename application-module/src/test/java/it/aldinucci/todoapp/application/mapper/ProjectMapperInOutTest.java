package it.aldinucci.todoapp.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectDTOOut;

class ProjectMapperInOutTest {
	
	private ProjectMapperInOut mapper;

	@Test
	void test_mapper() {
		mapper = new ProjectMapperInOut();
		NewProjectDTOIn dtoIn = new NewProjectDTOIn("name", "user@email.it");
		NewProjectDTOOut dtoOut = mapper.map(dtoIn);
		
		assertThat(dtoOut.getName()).isEqualTo(dtoIn.getName());
		assertThat(dtoOut.getUserEmail()).isEqualTo(dtoIn.getUserEmail());
	}

}
