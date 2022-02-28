package it.aldinucci.todoapp.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.application.port.out.dto.NewUserDTOOut;

class UserMapperInOutTest {

	private UserMapperInOut mapper;
	
	@Test
	void test_mapper() {
		mapper = new UserMapperInOut();
		NewUserDTOOut dtoOut = mapper.map(new NewUserDTOIn("username", "test@email.it", "password"));
		
		assertThat(dtoOut.getUsername()).isEqualTo("username");
		assertThat(dtoOut.getEmail()).isEqualTo("test@email.it");
		assertThat(dtoOut.getPassword()).isEqualTo("password");
	}

}
