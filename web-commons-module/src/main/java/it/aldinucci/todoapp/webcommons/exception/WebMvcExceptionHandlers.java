package it.aldinucci.todoapp.webcommons.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class WebMvcExceptionHandlers extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(UnauthorizedWebAccessException.class)
	public ResponseEntity<String> illegalHandler(HttpServletRequest request, Throwable ex) {
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

}
