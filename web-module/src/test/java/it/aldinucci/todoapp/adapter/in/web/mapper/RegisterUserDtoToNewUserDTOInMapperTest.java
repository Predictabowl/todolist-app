package it.aldinucci.todoapp.adapter.in.web.mapper;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.webcommons.dto.RegisterUserDto;

class RegisterUserDtoToNewUserDTOInMapperTest {

	private RegisterUserDtoToNewUserDTOInMapper mapper;
	
	@Test
	void test() {
		mapper = new RegisterUserDtoToNewUserDTOInMapper();
		
		NewUserDTOIn userDTOIn = mapper.map(new RegisterUserDto("test@email.it", "username", "password1", "password2"));
		
		assertThat(userDTOIn.getEmail()).isEqualTo("test@email.it");
		assertThat(userDTOIn.getPassword()).isEqualTo("password1");
		assertThat(userDTOIn.getUsername()).isEqualTo("username");
	}

}
