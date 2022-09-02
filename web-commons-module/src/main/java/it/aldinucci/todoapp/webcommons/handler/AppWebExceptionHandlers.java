package it.aldinucci.todoapp.webcommons.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import it.aldinucci.todoapp.exception.AppProjectNotFoundException;
import it.aldinucci.todoapp.exception.AppTaskNotFoundException;
import it.aldinucci.todoapp.exception.AppUserNotFoundException;
import it.aldinucci.todoapp.webcommons.exception.ForbiddenWebAccessException;

@ControllerAdvice
public class AppWebExceptionHandlers extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(ForbiddenWebAccessException.class)
	public ResponseEntity<String> forbiddenAccessHandler(HttpServletRequest request, Throwable ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler({AppUserNotFoundException.class, AppProjectNotFoundException.class, AppTaskNotFoundException.class})
	public ResponseEntity<String> entityNotFoundHandler(HttpServletRequest request, Throwable ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
}
