package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({IsUserPresentJPA.class})
class IsUserPresentJPATest {

	@Autowired
	private IsUserPresentJPA sutAdapter;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@MockBean
	private AppGenericMapper<UserJPA, User> mapper;
	
	@Test
	void test_whenUserNotPresent_shouldReturnFalse() {
		boolean isPresent = sutAdapter.isPresent("fakemail@email.com");
		
		assertThat(isPresent).isFalse();
	}
	
	@Test
	void test_whenUserPresent_shouldReturnTrue() {
		entityManager.persist(new UserJPA("test@email.com", "name", "pass"));
		
		boolean isPresent = sutAdapter.isPresent("test@email.com");
		
		assertThat(isPresent).isTrue();
	}
}
