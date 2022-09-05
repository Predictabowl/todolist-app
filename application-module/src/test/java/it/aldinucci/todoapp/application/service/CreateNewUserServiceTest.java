package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDtoOut;
import it.aldinucci.todoapp.application.port.out.CreateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByIdDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewUserData;
import it.aldinucci.todoapp.application.service.util.CreateVerificationToken;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exception.AppEmailAlreadyRegisteredException;
import it.aldinucci.todoapp.util.AppPasswordEncoder;

@ExtendWith(SpringExtension.class)
class CreateNewUserServiceTest {

	private static final String FIXTURE_EMAIL = "test@email.it";

	@Mock
	private CreateUserDriverPort createUser;
	
	@Mock
	private AppPasswordEncoder encoder;
	
	@Mock 
	private LoadUserByIdDriverPort loadUser;
	
	@Mock
	private CreateVerificationToken createToken;
	
	private CreateNewUserService service;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
		service = new CreateNewUserService(createUser, encoder, loadUser, createToken);
	}
	
	@Test
	void test_createUserSuccess_shouldCallPort() throws AppEmailAlreadyRegisteredException {
		NewUserDTOIn newUserIn = new NewUserDTOIn("name", FIXTURE_EMAIL, "password");
		User createdUser = new User(FIXTURE_EMAIL, "user", "pass");
		VerificationToken token = new VerificationToken();
		when(createUser.create(isA(NewUserData.class))).thenReturn(createdUser);
		when(encoder.encode(anyString())).thenReturn("encoded password");
		when(loadUser.load(anyString())).thenReturn(Optional.empty());
		when(createToken.create(anyString())).thenReturn(token);
		
		NewUserDtoOut newUserDtoOut = service.create(newUserIn);
		
		assertThat(newUserDtoOut.user()).isSameAs(createdUser);
		assertThat(newUserDtoOut.token()).isSameAs(token);
		InOrder inOrder = Mockito.inOrder(createUser,encoder, loadUser, createToken);
		inOrder.verify(loadUser).load(FIXTURE_EMAIL);
		inOrder.verify(encoder).encode("password");
		inOrder.verify(createUser).create(new NewUserData("name", FIXTURE_EMAIL, "encoded password"));
		inOrder.verify(createToken).create(FIXTURE_EMAIL);
	}
	
	@Test
	void test_createUserWhenEmailAlreadyPresent_shouldThrow() {
		NewUserDTOIn newUserIn = new NewUserDTOIn("name", FIXTURE_EMAIL, "password");
		User oldUser = new User("test@email.it", "user", "pass");
		when(loadUser.load(anyString())).thenReturn(Optional.of(oldUser));
		
		assertThatThrownBy(() -> service.create(newUserIn))
			.isInstanceOf(AppEmailAlreadyRegisteredException.class)
			.hasMessage("There's already an user registered with the email: test@email.it");
		
		verify(loadUser).load(FIXTURE_EMAIL);
		verifyNoInteractions(encoder);
		verifyNoInteractions(createUser);
		verifyNoInteractions(createToken);
	}

}
