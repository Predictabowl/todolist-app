package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import({LoadUserByEmailJPA.class, ModelMapper.class})
class LoadUserByEmailJPATest {
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private LoadUserByEmailJPA loadAdapter;
	
	@Autowired
	private TestEntityManager entityManager;

	@Test
	void test_loadUser_successful() {
		UserJPA user = new UserJPA("email", "username", "password");
		entityManager.persistAndFlush(user);
		
		User loadedUser = loadAdapter.load("email");
		
		assertThat(loadedUser).usingRecursiveComparison().isEqualTo(
				mapper.map(user, User.class));
	}

	@Test
	void test_loadUser_whenUserNotPresent() {
		assertThatThrownBy(() -> loadAdapter.load("email"))
			.isExactlyInstanceOf(AppUserNotFoundException.class)
			.hasMessage("User not found with email: email");
	}
}
