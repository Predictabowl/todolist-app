package it.aldinucci.todoapp.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import it.aldinucci.todoapp.application.port.in.dto.NewUserDTOIn;
import it.aldinucci.todoapp.application.port.out.CreateUserDriverPort;
import it.aldinucci.todoapp.application.port.out.dto.NewUserDTOOut;
import it.aldinucci.todoapp.domain.User;
import it.aldinucci.todoapp.mapper.AppGenericMapper;

class CreateNewUserServiceTest {

	@Mock
	private AppGenericMapper<NewUserDTOIn, NewUserDTOOut> mapper;
	
	@Mock
	private CreateUserDriverPort driverPort;
	
	@Mock
	private PasswordEncoder encoder;
	
	@InjectMocks
	private CreateNewUserService service;
	
	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_serviceShouldCallPort() {
		NewUserDTOIn newUserIn = new NewUserDTOIn("name", "test@€mail.it", "password");
		NewUserDTOOut newUserOut = spy(new NewUserDTOOut("test", "user@email.it", "pass"));
		User createdUser = new User("email", "user", "pass");
		when(driverPort.create(isA(NewUserDTOOut.class))).thenReturn(createdUser);
		when(mapper.map(newUserIn)).thenReturn(newUserOut);
		when(encoder.encode(isA(CharSequence.class))).thenReturn("encoded password");
		
		User resultUser = service.create(newUserIn);
		
		InOrder inOrder = Mockito.inOrder(driverPort,mapper,encoder,newUserOut);
		inOrder.verify(mapper).map(newUserIn);
		inOrder.verify(encoder).encode("pass");
		inOrder.verify(newUserOut).setPassword("encoded password");
		inOrder.verify(driverPort).create(newUserOut);
		assertThat(resultUser).isSameAs(createdUser);
	}

}
