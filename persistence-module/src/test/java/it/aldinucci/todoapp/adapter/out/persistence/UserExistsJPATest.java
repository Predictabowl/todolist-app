package it.aldinucci.todoapp.adapter.out.persistence;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(UserExistsJPA.class)
class UserExistsJPATest {

	@Autowired
	TestEntityManager entityManager;
	
	@Autowired
	private UserExistsJPA sut;
	
	@Test
	void test_whenUser_notExists() {
		
		boolean exists = sut.exists("email@test.it");
		
		assertThat(exists).isFalse();
	}
	
	@Test
	void test_whenUser_exists() {
		entityManager.persistAndFlush(new UserJPA("test@email.it", "name", "password"));
		
		boolean exists = sut.exists("test@email.it");
		
		assertThat(exists).isTrue();
	}

}
