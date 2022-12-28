package it.aldinucci.todoapp.adapter.in.rest.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.application.port.in.UpdateUserDataUsePort;
import it.aldinucci.todoapp.application.port.in.dto.UserDataDTOIn;
import it.aldinucci.todoapp.application.port.in.dto.UserIdDTO;
import it.aldinucci.todoapp.webcommons.dto.UserDataWebDto;

@RestController
@RequestMapping("/api/user")
public class UpdateUserDataRestController {

	private final UpdateUserDataUsePort updateUser;
	
	public UpdateUserDataRestController(UpdateUserDataUsePort updateUser) {
		super();
		this.updateUser = updateUser;
	}
 

	@PutMapping
	public ResponseEntity<Void> updateUserDataEndpoint(Authentication authentication,
				@Valid @RequestBody UserDataWebDto userDataWebDto){
		
		if (updateUser.update(new UserIdDTO(
				authentication.getName()), 
				new UserDataDTOIn(userDataWebDto.username())).isEmpty())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
