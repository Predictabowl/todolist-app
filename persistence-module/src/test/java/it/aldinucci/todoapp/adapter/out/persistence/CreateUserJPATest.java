package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.application.port.out.dto.NewUserData;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@Import(CreateUserJPA.class)
class CreateUserJPATest {

	@MockBean
	private AppGenericMapper<UserJPA, User> userMapper;
	
	@MockBean
	private AppGenericMapper<NewUserData, UserJPA> userJPAMapper;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private CreateUserJPA createUser;
	
	@Test
	void test_createNewUser_success() {
		NewUserData userDto = new NewUserData("username", "test@email.it", "password");
		UserJPA newUserJpa = new UserJPA("email@test.it", "name", "password");
		User newUser = new User("user@email.it", "username", "pass");
		when(userMapper.map(isA(UserJPA.class))).thenReturn(newUser);
		when(userJPAMapper.map(isA(NewUserData.class))).thenReturn(newUserJpa);
		
		User createdUser = createUser.create(userDto);
		
		assertThat(createdUser).isSameAs(newUser);
		List<UserJPA> usersJpa = entityManager.getEntityManager().createQuery("from UserJPA",UserJPA.class).getResultList();
		assertThat(usersJpa).hasSize(1);
		UserJPA userJpa = usersJpa.get(0);
		verify(userMapper).map(userJpa);
		verify(userJPAMapper).map(userDto);
		assertThat(userJpa.getEmail()).isEqualTo("email@test.it");
		assertThat(userJpa.getPassword()).isEqualTo("password");
		assertThat(userJpa.getUsername()).isEqualTo("name");
		assertThat(userJpa.getId()).isNotNull();
		
	}

}
