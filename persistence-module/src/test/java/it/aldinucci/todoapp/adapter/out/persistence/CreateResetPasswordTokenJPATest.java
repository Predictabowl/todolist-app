package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import it.aldinucci.todoapp.application.port.out.dto.ResetPasswordTokenData;
import it.aldinucci.todoapp.domain.ResetPasswordToken;
import it.aldinucci.todoapp.exception.AppUserAlreadyHaveResetPasswordTokenException;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(CreateResetPasswordTokenJPA.class)
class CreateResetPasswordTokenJPATest {

	@MockBean
	private AppGenericMapper<ResetPasswordTokenJPA, ResetPasswordToken> mapper;
	
	@Autowired
	private CreateResetPasswordTokenJPA sut;

	@Autowired
	private TestEntityManager entityManager;
	
	
	private Date date;

	@BeforeEach
	void setUp() {
		date = Calendar.getInstance().getTime();
	}

	@Test
	void test_createToken_whenDoesNotExists() {
		UserJPA user = new UserJPA("user@email.it", "username", "pass");
		entityManager.persistAndFlush(user);
		ResetPasswordTokenData dto = new ResetPasswordTokenData("token", date, "user@email.it");
		ResetPasswordToken token = new ResetPasswordToken();
		when(mapper.map(isA(ResetPasswordTokenJPA.class))).thenReturn(token);

		ResetPasswordToken createdToken = sut.create(dto);

		List<ResetPasswordTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from ResetPasswordTokenJPA", ResetPasswordTokenJPA.class).getResultList();
		
		assertThat(tokens).hasSize(1);
		ResetPasswordTokenJPA tokenJPA = tokens.get(0);
		assertThat(tokenJPA.getExpiryDate()).isEqualTo(date);
		assertThat(tokenJPA.getToken()).isEqualTo("token");
		assertThat(tokenJPA.getUser()).isEqualTo(user);
		assertThat(createdToken).isSameAs(token);
	}
	
	@Test
	void test_createToken_whenUserAlreadyHaveAToken_shouldThrow() {
		UserJPA user = new UserJPA("user@email.it", "username", "pass");
		entityManager.persistAndFlush(user);
		ResetPasswordTokenJPA tokenJpa = new ResetPasswordTokenJPA("token", user, date);
		entityManager.persistAndFlush(tokenJpa);
		ResetPasswordTokenData dto = new ResetPasswordTokenData("another token", date, "user@email.it");
		
		assertThatThrownBy(() -> sut.create(dto))
			.isInstanceOf(AppUserAlreadyHaveResetPasswordTokenException.class);

		List<ResetPasswordTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from ResetPasswordTokenJPA", ResetPasswordTokenJPA.class).getResultList();
		
		assertThat(tokens).hasSize(1);
		verifyNoInteractions(mapper);
	}
	
	@Test
	void test_createToken_whenUserNotExists_shouldThrow() {
		ResetPasswordTokenData dto = new ResetPasswordTokenData("another token", date, "user@email.it");
		
		assertThatThrownBy(() -> sut.create(dto))
			.isInstanceOf(AppUserNotFoundException.class)
			.hasMessage("User not found with email: user@email.it");

		List<ResetPasswordTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from ResetPasswordTokenJPA", ResetPasswordTokenJPA.class).getResultList();
		
		assertThat(tokens).isEmpty();
		verifyNoInteractions(mapper);
	}
}
