package it.aldinucci.todoapp.adapter.in.rest.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import it.aldinucci.todoapp.adapter.in.rest.controller.CreateProjectRestController;
import it.aldinucci.todoapp.exceptions.UserNotFoundException;

@ControllerAdvice(basePackageClasses = {CreateProjectRestController.class})
public class RestControllersAdvice extends ResponseEntityExceptionHandler {

	@ResponseBody
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> hadleUserExceptions(HttpServletRequest request, Throwable ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

}
