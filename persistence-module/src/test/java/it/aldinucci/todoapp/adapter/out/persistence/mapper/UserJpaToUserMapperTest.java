package it.aldinucci.todoapp.adapter.out.persistence.mapper;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.domain.User;

class UserJpaToUserMapperTest {

	private UserJpaToUserMapper mapper;
	
	@BeforeEach
	void setUp() {
		mapper = new UserJpaToUserMapper();
	}
	
	@Test
	void test() {
		User user = mapper.map(new UserJPA(2L, "email", "username", "password"));
		
		assertThat(user.getEmail()).isEqualTo("email");
		assertThat(user.getPassword()).isEqualTo("password");
		assertThat(user.getUsername()).isEqualTo("username");
	}

}
