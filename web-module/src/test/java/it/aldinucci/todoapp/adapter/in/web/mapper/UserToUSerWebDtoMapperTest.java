package it.aldinucci.todoapp.adapter.in.web.mapper;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import it.aldinucci.todoapp.adapter.in.web.dto.UserWebDto;
import it.aldinucci.todoapp.domain.User;

class UserToUSerWebDtoMapperTest {

	private UserToUSerWebDtoMapper mapper;

	@Test
	void test() {
		mapper = new UserToUSerWebDtoMapper();

		UserWebDto webDto = mapper.map(new User("test@email.it", "username", "password"));

		assertThat(webDto.getEmail()).isEqualTo("test@email.it");
		assertThat(webDto.getUsername()).isEqualTo("username");
	}

}
