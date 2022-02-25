package it.aldinucci.todoapp.webcommons.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import it.aldinucci.todoapp.webcommons.exception.ForbiddenWebAccessException;

@ControllerAdvice
public class AppCommonsExceptionHandlers extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(ForbiddenWebAccessException.class)
	public ResponseEntity<String> illegalHandler(HttpServletRequest request, Throwable ex) {
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

}
