package it.aldinucci.todoapp.adapter.out.persistence;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import it.aldinucci.todoapp.adapter.out.persistence.entity.ResetPasswordTokenJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.util.ValidateId;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.mapper.AppGenericMapper;


@DataJpaTest
@Import(LoadResetPasswordTokenJPA.class)
class LoadResetPasswordTokenJPATest {

	@MockBean
	private AppGenericMapper<ResetPasswordTokenJPA, ResetPasswordToken> mapper;
	
	@Autowired
	private LoadResetPasswordTokenJPA loadToken;
	
	@Autowired
	TestEntityManager entityManager;
	
	@MockBean
	private ValidateId<UUID> validator;
	
	@Test
	void test_loadToken_whenInvalidToken() {
		when(validator.getValidId(anyString()))
			.thenReturn(Optional.empty());
		
		assertThat(loadToken.load("token")).isEmpty();
		
		verify(validator).getValidId("token");
	}
	
	@Test
	void test_loadToken_whenMissing() {
		when(validator.getValidId(anyString()))
			.thenReturn(Optional.of(UUID.randomUUID()));
		
		assertThat(loadToken.load("some id")).isEmpty();
		
		verify(validator).getValidId("some id");
	}
	
	@Test
	void test_loadToken_success() {
		UserJPA user = new UserJPA("email", "name", "pass");
		entityManager.persistAndFlush(user);
		Date date = Calendar.getInstance().getTime();
		ResetPasswordTokenJPA tokenJpa = new ResetPasswordTokenJPA(user, date);
		entityManager.persistAndFlush(tokenJpa);
		ResetPasswordToken token = new ResetPasswordToken("token", date, "email");
		when(mapper.map(isA(ResetPasswordTokenJPA.class))).thenReturn(token);
		when(validator.getValidId(anyString()))
			.thenReturn(Optional.of(tokenJpa.getToken()));
		
		Optional<ResetPasswordToken> loadedToken = loadToken.load("some id");
		
		assertThat(loadedToken).contains(token);
		verify(validator).getValidId("some id");
	}
}
