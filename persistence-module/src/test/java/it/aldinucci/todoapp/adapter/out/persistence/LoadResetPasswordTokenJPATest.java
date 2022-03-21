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

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.mapper.AppGenericMapper;


@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(LoadResetPasswordTokenJPA.class)
class LoadResetPasswordTokenJPATest {

	@MockBean
	private AppGenericMapper<ResetPasswordTokenJPA, ResetPasswordToken> mapper;
	
	@Autowired
	private LoadResetPasswordTokenJPA loadToken;
	
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
		ResetPasswordTokenJPA tokenJpa = new ResetPasswordTokenJPA("token", user, date);
		entityManager.persistAndFlush(tokenJpa);
		ResetPasswordToken token = new ResetPasswordToken("token", date, "email");
		when(mapper.map(isA(ResetPasswordTokenJPA.class))).thenReturn(token);
		
		Optional<ResetPasswordToken> loadedToken = loadToken.load("token");
		
		assertThat(loadedToken).contains(token);
	}
}
