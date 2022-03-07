package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.NewUserDtoOut;
import it.aldinucci.todoapp.application.port.out.CreateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.LoadUserByEmailDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewUserData;
import it.aldinucci.todoapp.application.service.util.CreateVerificationToken;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.domain.VerificationToken;
import it.aldinucci.todoapp.exceptions.AppEmailAlreadyRegisteredException;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

class CreateNewUserServiceTest {

	private static final String FIXTURE_EMAIL = "test@email.it";

	@Mock
	private AppGenericMapper<NewUserDTOIn, NewUserData> mapper;
	
	@Mock
	private CreateUserDriverPort createUser;
	
	@Mock
	private PasswordEncoder encoder;
	
	@Mock 
	private LoadUserByEmailDriverPort loadUser;
	
	@Mock
	private CreateVerificationToken createToken;
	
	@InjectMocks
	private CreateNewUserService service;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_createUserSuccess_shouldCallPort() throws AppEmailAlreadyRegisteredException {
		NewUserDTOIn newUserIn = new NewUserDTOIn("name", FIXTURE_EMAIL, "password");
		NewUserData newUserOut = spy(new NewUserData("test", "user@email.it", "pass"));
		User createdUser = new User("email", "user", "pass");
		VerificationToken token = new VerificationToken();
		when(createUser.create(isA(NewUserData.class))).thenReturn(createdUser);
		when(mapper.map(newUserIn)).thenReturn(newUserOut);
		when(encoder.encode(isA(CharSequence.class))).thenReturn("encoded password");
		when(loadUser.load(isA(String.class))).thenReturn(Optional.empty());
		when(createToken.create(anyString())).thenReturn(token);
		
		NewUserDtoOut newUserDtoOut = service.create(newUserIn);
		
		assertThat(newUserDtoOut.getUser()).isSameAs(createdUser);
		assertThat(newUserDtoOut.getToken()).isSameAs(token);
		InOrder inOrder = Mockito.inOrder(createUser,mapper,encoder,newUserOut,loadUser, createToken);
		inOrder.verify(loadUser).load(FIXTURE_EMAIL);
		inOrder.verify(mapper).map(newUserIn);
		inOrder.verify(encoder).encode("pass");
		inOrder.verify(newUserOut).setPassword("encoded password");
		inOrder.verify(createUser).create(newUserOut);
		inOrder.verify(createToken).create(FIXTURE_EMAIL);
	}
	
	@Test
	void test_createUserWhenEmailAlreadyPresent_shouldThrow() {
		NewUserDTOIn newUserIn = new NewUserDTOIn("name", FIXTURE_EMAIL, "password");
		User oldUser = new User("email", "user", "pass");
		when(loadUser.load(isA(String.class))).thenReturn(Optional.of(oldUser));
		
		assertThatThrownBy(() -> service.create(newUserIn))
			.isInstanceOf(AppEmailAlreadyRegisteredException.class)
			.hasMessage("There's already an user registered with the email: test@email.it");
		
		verify(loadUser).load(FIXTURE_EMAIL);
		verifyNoInteractions(mapper);
		verifyNoInteractions(encoder);
		verifyNoInteractions(createUser);
		verifyNoInteractions(createToken);
	}

}
