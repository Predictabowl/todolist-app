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
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class VerificationTokenJPARepositoryTest {

	@Autowired
	private VerificationTokenJPARepository repository;
	
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
		VerificationTokenJPA token = new VerificationTokenJPA("code", user, calendar.getTime());
		entityManager.persist(token);
		
		VerificationTokenJPA loadedToken = repository.findByToken("code").get();
		
		assertThat(loadedToken).usingRecursiveComparison().isEqualTo(token);
	}
	
	@Test
	void test_findByUser_successful() {
		Calendar calendar = Calendar.getInstance();
		VerificationTokenJPA token = new VerificationTokenJPA("code", user, calendar.getTime());
		entityManager.persist(token);
		
		VerificationTokenJPA loadedToken = repository.findByUser(user).get();
		
		assertThat(loadedToken).usingRecursiveComparison().isEqualTo(token);
	}
	
	@Test
	void test_mapping_oneToOne() {
		Calendar calendar = Calendar.getInstance();
		VerificationTokenJPA token = new VerificationTokenJPA("code", user, calendar.getTime());
		entityManager.persist(token);
		VerificationTokenJPA token2 = new VerificationTokenJPA("code2", user, calendar.getTime());

		assertThatThrownBy(() -> repository.save(token2))
			.isInstanceOf(DataIntegrityViolationException.class)
			.hasCauseInstanceOf(EntityExistsException.class);
		
	}
	
}
