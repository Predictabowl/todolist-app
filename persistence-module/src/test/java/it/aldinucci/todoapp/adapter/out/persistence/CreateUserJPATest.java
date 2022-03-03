package it.aldinucci.todoapp.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.adapter.out.persistence.entity.UserJPA;
import it.aldinucci.todoapp.application.port.out.dto.NewUserDTOOut;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(CreateUserJPA.class)
class CreateUserJPATest {

	@MockBean
	private AppGenericMapper<UserJPA, User> userMapper;
	
	@MockBean
	private AppGenericMapper<NewUserDTOOut, UserJPA> userJPAMapper;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private CreateUserJPA createUser;
	
	@Test
	void test_createNewUser_success() {
		NewUserDTOOut userDto = new NewUserDTOOut("username", "test@email.it", "password");
		UserJPA newUserJpa = new UserJPA("email@test.it", "name", "password");
		User newUser = new User("user@email.it", "username", "pass");
		when(userMapper.map(isA(UserJPA.class))).thenReturn(newUser);
		when(userJPAMapper.map(isA(NewUserDTOOut.class))).thenReturn(newUserJpa);
		
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
