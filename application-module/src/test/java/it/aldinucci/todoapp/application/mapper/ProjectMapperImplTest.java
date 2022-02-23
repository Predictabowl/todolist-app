package it.aldinucci.todoapp.application.mapper;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewProjectDTOOut;

class ProjectMapperImplTest {

	private ProjectMapperImpl mapper;
	
	@Test
	void test_mapper() {
		mapper = new ProjectMapperImpl();
		NewProjectDTOIn dtoIn = new NewProjectDTOIn("name", "email");
		NewProjectDTOOut dtoOut = mapper.map(dtoIn);
		
		assertThat(dtoOut.getName()).isEqualTo(dtoIn.getName());
		assertThat(dtoOut.getUserEmail()).isEqualTo(dtoIn.getUserEmail());
	}

}
