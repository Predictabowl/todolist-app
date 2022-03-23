package it.aldinucci.todoapp.application.mapper;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.application.port.out.dto.UserData;
import it.aldinucci.todoapp.domain.User;

class UserToUserDataMapperTest {

	@Test
	void test() {
		UserToUserDataMapper mapper = new UserToUserDataMapper();
		User user = new User("email@test.it", "name", "password", true);
		
		UserData userData = mapper.map(user);
		
		assertThat(userData.email()).matches("email@test.it");
		assertThat(userData.enabled()).isTrue();
		assertThat(userData.password()).matches("password");
		assertThat(userData.username()).matches("name");
	}

}
