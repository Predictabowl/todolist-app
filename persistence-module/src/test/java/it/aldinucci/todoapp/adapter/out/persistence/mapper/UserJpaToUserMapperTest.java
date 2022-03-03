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
	void test_userEnabled() {
		UserJPA userJPA = new UserJPA(2L, "email", "username", "password");
		userJPA.setEnabled(true);
		User user = mapper.map(userJPA);
		
		assertThat(user.getEmail()).isEqualTo("email");
		assertThat(user.getPassword()).isEqualTo("password");
		assertThat(user.getUsername()).isEqualTo("username");
		assertThat(user.isEnabled()).isTrue();
	}
	
	@Test
	void test_userNotEnabled() {
		UserJPA userJPA = new UserJPA(3L, "email@test.it", "name", "pass");
		User user = mapper.map(userJPA);
		
		assertThat(user.getEmail()).isEqualTo("email@test.it");
		assertThat(user.getPassword()).isEqualTo("pass");
		assertThat(user.getUsername()).isEqualTo("name");
		assertThat(user.isEnabled()).isFalse();
	}

}
