package it.aldinucci.todoapp.adapter.out.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class UserJPARepositoryTest {

	@Autowired
	private UserJPARepository repository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	void test_findByEmail_successful() {
		UserJPA user = new UserJPA(null, "email@test.it", "test", "password", new LinkedList<ProjectJPA>());
		entityManager.persist(user);
		
		UserJPA loadedUser = repository.findByEmail("email@test.it").get();
		
		assertThat(loadedUser).usingRecursiveComparison().isEqualTo(user);
	}
	
	@Test
	void test_emailShouldBeUnique() {
		UserJPA user = new UserJPA(null, "email@test.it", "test", "password", new LinkedList<ProjectJPA>());
		entityManager.persistAndFlush(user);
		UserJPA secondUser = new UserJPA(null, "email@test.it", "username", "nothing", new LinkedList<ProjectJPA>());
		
		assertThatThrownBy(() -> repository.saveAndFlush(secondUser))
			.isInstanceOf(DataIntegrityViolationException.class);
	}
	
	@Test
	void test_findByEmail_whenNotPresent() {
		Optional<UserJPA> user = repository.findByEmail("test@email.it");
		
		assertThat(user).isEmpty();
	}

}
