package it.aldinucci.todoapp.adapter.in.rest.controller;

import static it.aldinucci.todoapp.webcommons.config.AppBaseURIs.BASE_REST_URI;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.aldinucci.todoapp.adapter.in.rest.dto.NewProjectRestDto;
import it.aldinucci.todoapp.application.port.in.CreateProjectUsePort;
import it.aldinucci.todoapp.application.port.in.dto.NewProjectDTOIn;
import it.aldinucci.todoapp.domain.Project;
import it.aldinucci.todoapp.exceptions.AppUserNotFoundException;

@RestController
@RequestMapping(BASE_REST_URI)
public class CreateProjectRestController {

	private CreateProjectUsePort createProject;
	
	@Autowired
	public CreateProjectRestController(CreateProjectUsePort createProject) {
		this.createProject = createProject;
	}

	@PostMapping("/project/create")
	public Project createProjectEndPoint(Authentication authentication, @Valid @RequestBody NewProjectRestDto newProject) {
		NewProjectDTOIn projectDto = new NewProjectDTOIn(newProject.getName(), authentication.getName());
		return createProject.create(projectDto);
	}
	
//	@PostMapping("/project/create")
//	public Project createProjectEndPoint(@Valid @RequestBody NewProjectRestDto newProject) {
//		NewProjectDTOIn projectDto = new NewProjectDTOIn(newProject.getName(), "user@email.com");
//		return createProject.create(projectDto);
//	}
//	
	@ExceptionHandler(AppUserNotFoundException.class)
	public ResponseEntity<String> userNotFoundHandler(HttpServletRequest request, Throwable ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
