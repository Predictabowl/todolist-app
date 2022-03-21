package it.aldinucci.todoapp.adapter.out.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Calendar;
import java.util.LinkedList;

import javax.persistence.EntityExistsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ProjectJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class ResetPasswordTokenJPARepositoryTest {

	@Autowired
	private ResetPasswordTokenJPARepository repository;
	
	@Autowired
	private TestEntityManager entityManager;

	private UserJPA user;
	
	@BeforeEach
	void setUp() {
		user = new UserJPA(null, "email@test.it", "test", "password", true, new LinkedList<ProjectJPA>());
		entityManager.persist(user);
	}
	
	@Test
	void test_findByToken_successful() {
		Calendar calendar = Calendar.getInstance();
		ResetPasswordTokenJPA token = new ResetPasswordTokenJPA("code", user, calendar.getTime());
		entityManager.persist(token);
		
		ResetPasswordTokenJPA loadedToken = repository.findByToken("code").get();
		
		assertThat(loadedToken).usingRecursiveComparison().isEqualTo(token);
	}
	

	@Test
	void test_findByUserEmail_successful() {
		Calendar calendar = Calendar.getInstance();
		ResetPasswordTokenJPA token = new ResetPasswordTokenJPA("code", user, calendar.getTime());
		entityManager.persistAndFlush(token);

		ResetPasswordTokenJPA loadedToken = repository.findByUserEmail("email@test.it").get();
		
		assertThat(loadedToken).usingRecursiveComparison().isEqualTo(token);
	}
	

	@Test
	void test_mapping_oneToOne() {
		Calendar calendar = Calendar.getInstance();
		ResetPasswordTokenJPA token = new ResetPasswordTokenJPA("code", user, calendar.getTime());
		entityManager.persist(token);
		ResetPasswordTokenJPA token2 = new ResetPasswordTokenJPA("code2", user, calendar.getTime());

		assertThatThrownBy(() -> repository.save(token2))
			.isInstanceOf(DataIntegrityViolationException.class)
			.hasCauseInstanceOf(EntityExistsException.class);
		
	}
	
}
