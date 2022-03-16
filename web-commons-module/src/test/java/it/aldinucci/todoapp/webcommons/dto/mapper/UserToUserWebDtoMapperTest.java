package it.aldinucci.todoapp.webcommons.dto.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.webcommons.dto.UserWebDto;

class UserToUserWebDtoMapperTest {

	private UserToUserWebDtoMapper mapper;

	@Test
	void test() {
		mapper = new UserToUserWebDtoMapper();

		UserWebDto webDto = mapper.map(new User("test@email.it", "username", "password"));

		assertThat(webDto.email()).isEqualTo("test@email.it");
		assertThat(webDto.username()).isEqualTo("username");
	}

}
