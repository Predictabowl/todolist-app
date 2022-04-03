package it.aldinucci.todoapp.application.handler;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.aldinucci.todoapp.application.config.ApplicationBeansProvider;
import it.aldinucci.todoapp.exception.AppMessagingException;
import it.aldinucci.todoapp.exception.handler.ExceptionThrowingFunction;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationBeansProvider.class})
@EnableConfigurationProperties
@PropertySource(value = "classpath:test.mail.properties")
class MimeMessageHelperExceptionHandlerTest {

	@Autowired
	private MimeMessageHelperExceptionHandler sut;
	
	@Autowired
	private MimeMessage mimeMessage;
	
	@Mock
	private ExceptionThrowingFunction<MimeMessageHelper, MimeMessage, MessagingException> code;

	@BeforeEach
	void setUp() {
		openMocks(this);
	}
	
	@Test
	void test_executeCode() throws MessagingException {
		when(code.apply(any())).thenReturn(mimeMessage);
		
		MimeMessage createdMessage = sut.doItWithHandler(code);
		
		assertThat(createdMessage).isSameAs(mimeMessage);
		verify(code).apply(isA(MimeMessageHelper.class));
	}
	
	@Test
	void test_whenMessageException_throwAppMessagingException() throws MessagingException {
		doThrow(MessagingException.class).when(code).apply(any());
		
		assertThatThrownBy(() -> sut.doItWithHandler(code))
			.isInstanceOf(AppMessagingException.class)
			.hasCauseInstanceOf(MessagingException.class);
	}

}
