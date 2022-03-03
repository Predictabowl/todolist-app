package it.aldinucci.todoapp.adapter.out.persistence;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(LoadVerificationTokenJPA.class)
class LoadVerificationTokenJPATest {

	@MockBean
	private AppGenericMapper<VerificationTokenJPA, VerificationToken> mapper;
	
	@Autowired
	private LoadVerificationTokenJPA loadToken;
	
	@Autowired
	TestEntityManager entityManager;
	
	@Test
	void test_loadToken_whenMissing() {
		assertThat(loadToken.load("token")).isEmpty();
	}
	
	@Test
	void test_loadToken_success() {
		UserJPA user = new UserJPA("email", "name", "pass");
		entityManager.persistAndFlush(user);
		Date date = Calendar.getInstance().getTime();
		VerificationTokenJPA tokenJpa = new VerificationTokenJPA("token", user, date);
		entityManager.persistAndFlush(tokenJpa);
		VerificationToken token = new VerificationToken("token", date, "email");
		when(mapper.map(isA(VerificationTokenJPA.class))).thenReturn(token);
		
		Optional<VerificationToken> loadedToken = loadToken.load("token");
		
		assertThat(loadedToken).contains(token);
	}

}
