package it.aldinucci.todoapp.adapter.out.persistence;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@Import(LoadVerificationTokenByEmailJPA.class)
class LoadVerificationTokenByEmailJPATest {

	private static final String FIXTURE_EMAIL = "test@email.it"; 
	
	@MockBean
	private AppGenericMapper<VerificationTokenJPA, VerificationToken> mapper;
	
	@Autowired
	private LoadVerificationTokenByEmailJPA sut;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	void test_loadToken_whenMissing() {
		assertThat(sut.load(FIXTURE_EMAIL)).isEmpty();
	}
	
	@Test
	void test_loadToken_success() {
		UserJPA user = new UserJPA(FIXTURE_EMAIL, "name", "pass");
		entityManager.persistAndFlush(user);
		Date date = Calendar.getInstance().getTime();
		VerificationTokenJPA tokenJpa = new VerificationTokenJPA(user, date);
		entityManager.persistAndFlush(tokenJpa);
		VerificationToken token = new VerificationToken("token-code", date, "email");
		when(mapper.map(isA(VerificationTokenJPA.class))).thenReturn(token);
		
		Optional<VerificationToken> loadedToken = sut.load(FIXTURE_EMAIL);
		
		assertThat(loadedToken).contains(token);
	}

}
