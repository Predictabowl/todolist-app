package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.application.port.out.dto.NewUserData;

class NewUserDTOInToUserJPAMapperTest {

	private NewUserDTOInToUserJPAMapper mapper;
	
	@BeforeEach
	void setUp() {
		mapper = new NewUserDTOInToUserJPAMapper();
	}
	
	@Test
	void test() {
		UserJPA user = mapper.map(new NewUserData("user", "email", "password"));
		
		assertThat(user.getEmail()).isEqualTo("email");
		assertThat(user.getPassword()).isEqualTo("password");
		assertThat(user.getUsername()).isEqualTo("user");
		assertThat(user.getId()).isNull();
		assertThat(user.getProjects()).isEmpty();
		assertThat(user.isEnabled()).isFalse();
	}


}
