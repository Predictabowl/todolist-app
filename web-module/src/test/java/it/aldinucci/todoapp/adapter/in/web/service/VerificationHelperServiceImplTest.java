package it.aldinucci.todoapp.adapter.in.web.service;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.net.URISyntaxException;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import it.aldinucci.todoapp.application.port.in.CreateVerificationTokenUsePort;
import it.aldinucci.todoapp.application.port.in.SendVerificationEmailUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.application.port.in.dto.VerificantionLinkDTO;
import it.aldinucci.todoapp.domain.VerificationToken;

class VerificationHelperServiceImplTest {

	private static final String FIXTURE_TOKEN = "code-string";

	private static final String FIXTURE_EMAIL = "test@email.it";
	
	private static final String FIXTURE_BASE_URL= "http://someplace.org/user/register/verify";

	@Mock
	private CreateVerificationTokenUsePort createToken;
	
	@Mock
	private SendVerificationEmailUsePort sendMail;
	
	@InjectMocks
	private VerificationHelperServiceImpl service;
	
	@BeforeEach
	void setUp(){
		openMocks(this);
	}
	
	
	@Test
	void test_verificationBuildingProcedure() throws URISyntaxException {
		VerificationToken token = new VerificationToken(FIXTURE_TOKEN, Calendar.getInstance().getTime(), FIXTURE_EMAIL);
		VerificantionLinkDTO linkDto = new VerificantionLinkDTO(FIXTURE_BASE_URL+"/"+FIXTURE_TOKEN, FIXTURE_EMAIL);  
		when(createToken.create(isA(UserIdDTO.class))).thenReturn(token);
		
		service.sendVerifcationMail(FIXTURE_EMAIL, FIXTURE_BASE_URL);
		
		verify(createToken).create(new UserIdDTO(FIXTURE_EMAIL));
		verify(sendMail).send(linkDto);
	}

}
