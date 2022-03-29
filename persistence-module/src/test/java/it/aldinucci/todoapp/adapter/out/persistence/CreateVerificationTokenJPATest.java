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

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.adapter.out.persistence.entity.VerificationTokenJPA;
import it.aldinucci.todoapp.application.port.out.dto.VerificationTokenData;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppUserAlreadyHaveVerificationTokenException;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(CreateVerificationTokenJPA.class)
class CreateVerificationTokenJPATest {

	@MockBean
	private AppGenericMapper<VerificationTokenJPA, VerificationToken> mapper;
	
	@Autowired
	private CreateVerificationTokenJPA createToken;

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
		VerificationTokenData dto = new VerificationTokenData(date, "user@email.it");
		VerificationToken token = new VerificationToken();
		when(mapper.map(isA(VerificationTokenJPA.class))).thenReturn(token);

		VerificationToken createdToken = createToken.create(dto);

		List<VerificationTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from VerificationTokenJPA", VerificationTokenJPA.class).getResultList();
		
		assertThat(tokens).hasSize(1);
		VerificationTokenJPA tokenJPA = tokens.get(0);
		assertThat(tokenJPA.getExpiryDate()).isEqualTo(date);
		assertThat(tokenJPA.getToken()).isNotNull();
		assertThat(tokenJPA.getUser()).isEqualTo(user);
		assertThat(createdToken).isSameAs(token);
	}
	
	@Test
	void test_createToken_whenUserAlreadyHaveAToken_shouldThrow() {
		UserJPA user = new UserJPA("user@email.it", "username", "pass");
		entityManager.persistAndFlush(user);
		VerificationTokenJPA tokenJpa = new VerificationTokenJPA(user, date);
		entityManager.persistAndFlush(tokenJpa);
		VerificationTokenData dto = new VerificationTokenData(date, "user@email.it");
		
		assertThatThrownBy(() -> createToken.create(dto))
			.isInstanceOf(AppUserAlreadyHaveVerificationTokenException.class);

		List<VerificationTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from VerificationTokenJPA", VerificationTokenJPA.class).getResultList();
		
		assertThat(tokens).hasSize(1);
		verifyNoInteractions(mapper);
	}
	
	@Test
	void test_createToken_whenUserNotExists_shouldThrow() {
		VerificationTokenData dto = new VerificationTokenData(date, "user@email.it");
		
		assertThatThrownBy(() -> createToken.create(dto))
			.isInstanceOf(AppUserNotFoundException.class)
			.hasMessage("User not found with email: user@email.it");

		List<VerificationTokenJPA> tokens = entityManager.getEntityManager()
				.createQuery("from VerificationTokenJPA", VerificationTokenJPA.class).getResultList();
		
		assertThat(tokens).isEmpty();
		verifyNoInteractions(mapper);
	}
	
}
